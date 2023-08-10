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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class KlineService {

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
    }

    /**
     * 同步K线
     */
    public void syncKlines() {
        List<Symbol> symbols = symbolDao.listByStatus(SymbolStatusEnum.TRADING.code);
        for (Symbol symbol: symbols) {
            threadPoolExecutor.execute(()->{
                syncKline(symbol.getName());
            });
        }
    }

    /**
     * 同步K线
     */
    public void syncKline(String symbol) {
        klineDao.createTable(symbol);
        Kline latestKline = klineDao.getLatestKline(symbol);
        Date startTime = latestKline == null ? null : latestKline.getOpenTime();
        if (startTime == null) {
            startTime = DateUtil.parseDateTime("2021-01-01 00:00:00");
        } else {
            startTime = DateUtil.offsetSecond(startTime, 1);
        }
        KlineParam param = new KlineParam();
        param.setSymbol(symbol);
        param.setInterval(KlineTypeEnum.FIVE_MINUTE.code);
        param.setStartTime(startTime);
        param.setEndTime(new Date());
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
            klineDao.saveBatch(klines);
            log.info("sync kline, symbol:{}, startTime:{}, endTime:{}, size:{}",
                    symbol, firstKline.getOpenTime(), lastKline.getOpenTime(), klines.size());
            param.setStartTime(DateUtil.offsetSecond(lastKline.getOpenTime(),1));
            param.setEndTime(new Date());
        }
    }
}
