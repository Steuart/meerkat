package cc.jooylife.meerkat.core.service;

import cc.jooylife.meerkat.core.common.dto.KlineDto;
import cc.jooylife.meerkat.core.common.enums.KlineTypeEnum;
import cc.jooylife.meerkat.core.common.enums.SymbolStatusEnum;
import cc.jooylife.meerkat.core.common.param.KlineParam;
import cc.jooylife.meerkat.core.exchange.BinanceExchange;
import cc.jooylife.meerkat.core.repository.dao.KlineDao;
import cc.jooylife.meerkat.core.repository.dao.SymbolDao;
import cc.jooylife.meerkat.core.repository.entity.Kline;
import cc.jooylife.meerkat.core.repository.entity.Symbol;
import cc.jooylife.meerkat.core.util.JsonUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.binance.api.client.BinanceApiWebSocket;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KlineService {


    private final ConcurrentHashMap<String, BinanceApiWebSocket> WEB_SOCKET_CACHE = new ConcurrentHashMap<>();

    private final SymbolDao symbolDao;

    private final BinanceExchange binanceExchange;

    private final KlineDao klineDao;

    @Autowired
    public KlineService(SymbolDao symbolDao, BinanceExchange binanceExchange, KlineDao klineDao) {
        this.symbolDao = symbolDao;
        this.binanceExchange = binanceExchange;
        this.klineDao = klineDao;
    }

    private volatile ThreadPoolExecutor threadPoolExecutor;

    @PostConstruct
    public void init() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        int corePoolSize = cpuNum * 2+1;
        int maximumPoolSize = cpuNum * 4+1;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, queue, handler);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        // initListener(KlineTypeEnum.FIVE_MINUTE);
    }

    /**
     * 同步K线
     */
    public void syncKlineData(Date endDate) {
        List<Symbol> symbols = symbolDao.listByStatus(SymbolStatusEnum.TRADING.code);
        CountDownLatch countDownLatch = new CountDownLatch(symbols.size());
        for (Symbol symbol: symbols) {
            threadPoolExecutor.execute(()->{
                try {
                    syncKline(symbol.getName(), endDate);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("syncKlines error", e);
        }
    }

    /**
     * 同步K线
     */
    public void syncKline(String symbol, Date endDate) {
        klineDao.createTable(symbol);
        Kline latestKline = klineDao.getLatestKline(symbol);
        Date startTime = latestKline == null ? null : latestKline.getOpenTime();
        if (startTime == null) {
            startTime = DateUtil.parseDateTime("2021-01-01 00:00:00");
        }
        KlineParam param = new KlineParam();
        param.setSymbol(symbol);
        param.setInterval(KlineTypeEnum.FIVE_MINUTE.code);
        param.setStartTime(startTime);
        param.setEndTime(endDate);
        while (true) {
            List<KlineDto> klineDtos = binanceExchange.getKline(param);
            if (CollectionUtil.isEmpty(klineDtos)) {
                break;
            }
            List<Kline> klines = new ArrayList<>();
            for (KlineDto klineDto: klineDtos) {
                Kline kline = new Kline();
                BeanUtils.copyProperties(klineDto, kline);
                kline.setType(KlineTypeEnum.FIVE_MINUTE.code);
                klines.add(kline);
            }
            Kline lastKline = klines.get(klines.size() - 1);
            Kline firstKline = klines.get(0);
            klineDao.deleteByOpenTime(symbol, firstKline.getOpenTime(), lastKline.getOpenTime(), KlineTypeEnum.FIVE_MINUTE.code);
            try {
                klineDao.saveBatch(klines);
            } catch (Exception e) {
                log.error("sync kline exception, symbol:{}, startTime:{}, endTime:{}, data:{}",
                        symbol, firstKline.getOpenTime(), lastKline.getOpenTime(), JsonUtil.toJson(klines));
                break;
            }
            log.info("sync kline, symbol:{}, startTime:{}, endTime:{}, size:{}",
                    symbol, firstKline.getOpenTime(), lastKline.getOpenTime(), klines.size());
            param.setStartTime(DateUtil.offsetSecond(lastKline.getOpenTime(),1));
            param.setEndTime(new Date());
        }
    }

    /**
     * 检查websocket状态
     */
    public void reconnectWebsocket(KlineTypeEnum klineType) {
        List<Symbol> symbols = symbolDao.listByStatus(SymbolStatusEnum.TRADING.code);
        List<String> symbolNames = symbols.stream().map(Symbol::getName).collect(Collectors.toList());
        for (Map.Entry<String, BinanceApiWebSocket> entry: WEB_SOCKET_CACHE.entrySet()) {
            String channel = entry.getKey();
            List<String> channelList = Lists.newArrayList(channel.split(","));
            if(Collections.disjoint(channelList, symbolNames)) {;
                closeWebSocket(entry.getValue());
                WEB_SOCKET_CACHE.remove(channel);
            }
            BinanceApiWebSocket webSocket = entry.getValue();
            if (!webSocket.getListener().isClosing()) {
                continue;
            }
            BinanceApiWebSocket klineWebSocket = createKlineWebSocket(channel, klineType);
            WEB_SOCKET_CACHE.put(channel, klineWebSocket);
        }
    }

    /**
     * 初始化监听器
     */
    public void initListener(KlineTypeEnum klineType) {
        WEB_SOCKET_CACHE.forEach((k,v) -> closeWebSocket(v));
        WEB_SOCKET_CACHE.clear();
        List<Symbol> symbols = symbolDao.listByStatus(SymbolStatusEnum.TRADING.code);
        if (CollectionUtil.isEmpty(symbols)) {
            log.error("Not found Trading Symbol");
            return;
        }
        List<List<Symbol>> symbolLists = Lists.partition(symbols, 50);
        List<String> channels = Lists.newArrayList();
        for (List<Symbol> symbolList : symbolLists) {
            String channel = symbolList
                    .stream()
                    .map(Symbol::getName)
                    .map(String::toLowerCase)
                    .sorted()
                    .collect(Collectors.joining(","));
            channels.add(channel);
        }
        for (String channel : channels) {
            BinanceApiWebSocket webSocket = createKlineWebSocket(channel, klineType);
            WEB_SOCKET_CACHE.put(channel, webSocket);
        }
    }

    /**
     * 创建websocket
     */
    private BinanceApiWebSocket createKlineWebSocket(String channel, KlineTypeEnum typeEnum) {
        BinanceApiWebSocketClient webSocketClient = binanceExchange.getWebSocketClient();
        CandlestickInterval interval = KlineTypeEnum.getByCode(typeEnum);
        return webSocketClient.onCandlestickEvent(channel, interval, response -> {
            log.info("response:{}", JsonUtil.toJson(response));
        });
    }

    /**
     * 关闭websocket
     */
    private void closeWebSocket(BinanceApiWebSocket webSocket) {
        try {
            webSocket.close();
        } catch (IOException e) {
            log.error("Close web socket error", e);
        }
    }
}
