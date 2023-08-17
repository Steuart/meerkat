package cc.jooylife.meerkat.core.listener;

import cc.jooylife.meerkat.core.common.enums.SymbolStatusEnum;
import cc.jooylife.meerkat.core.exchange.BinanceExchange;
import cc.jooylife.meerkat.core.repository.dao.KlineDao;
import cc.jooylife.meerkat.core.repository.dao.SymbolDao;
import cc.jooylife.meerkat.core.repository.entity.Symbol;
import cc.jooylife.meerkat.core.util.JsonUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.MD5;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KlineListener {

    private final BinanceExchange binanceExchange;

    private final SymbolDao symbolDao;

    private KlineDao klineDao;

    private final ConcurrentHashMap<String, Closeable> CLOSEABLE_MAP = new ConcurrentHashMap<>();

    @Autowired
    public KlineListener(BinanceExchange binanceExchange, SymbolDao symbolDao, KlineDao klineDao) {
        this.binanceExchange = binanceExchange;
        this.symbolDao = symbolDao;
        this.klineDao = klineDao;
    }

    public void startListener() {
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
        BinanceApiWebSocketClient webSocketClient = binanceExchange.getWebSocketClient();
        BinanceApiConfig.useTestnetStreaming = true;
        for (String channel : channels) {
            String key = MD5.create().digestHex16(channel);
            log.info("register channel:{},key:{}", channel, key);
            Closeable closeable = webSocketClient.onCandlestickEvent(channel, CandlestickInterval.ONE_MINUTE, new BinanceApiCallback<>() {
                @Override
                public void onResponse(CandlestickEvent response) {
                    log.info("response:{}", JsonUtil.toJson(response));
                }

                @Override
                public void onClosing(int code, String reason) {
                    CLOSEABLE_MAP.remove(key);
                }
            });
            CLOSEABLE_MAP.put(key, closeable);
        }
    }
}
