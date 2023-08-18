package cc.jooylife.meerkat.core.test.exchange;

import cc.jooylife.meerkat.core.common.dto.KlineDto;
import cc.jooylife.meerkat.core.common.dto.SymbolDto;
import cc.jooylife.meerkat.core.common.enums.KlineTypeEnum;
import cc.jooylife.meerkat.core.common.param.KlineParam;
import cc.jooylife.meerkat.core.exchange.BinanceExchange;
import cc.jooylife.meerkat.core.test.Application;
import cc.jooylife.meerkat.core.util.JsonUtil;
import cn.hutool.core.date.DateUtil;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = Application.class)
@Slf4j
public class BinanceExchangeTest {

    @Autowired
    private BinanceExchange binanceExchange;

    @Test
    public void getKlines() {
        KlineParam param = new KlineParam();
        param.setSymbol("BTCUSDT");
        param.setInterval(KlineTypeEnum.FIVE_MINUTE.code);
        param.setStartTime(DateUtil.parseDateTime("2023-01-01 00:00:01"));
        param.setEndTime(DateUtil.parseDateTime("2023-01-01 00:05:00"));
        List<KlineDto> klines = binanceExchange.getKline(param);
        Assertions.assertEquals(1, klines.size());
        KlineDto klineDto = klines.get(0);
        Assertions.assertEquals(DateUtil.parseDateTime("2023-01-01 00:05:00"), klineDto.getOpenTime());
        Assertions.assertEquals(DateUtil.parseDateTime("2023-01-01 00:09:59"), klineDto.getCloseTime());
        Assertions.assertEquals(new BigDecimal("16591.91000000"), klineDto.getOpen());
        Assertions.assertEquals(new BigDecimal("16595.98000000"), klineDto.getHigh());
        Assertions.assertEquals(new BigDecimal("16591.09000000"), klineDto.getLow());
        Assertions.assertEquals(new BigDecimal("16592.91000000"), klineDto.getClose());
        Assertions.assertEquals(new BigDecimal("230.75273000"), klineDto.getVolume());
        Assertions.assertEquals(new BigDecimal("3828828.02069570"), klineDto.getQuJoteAssetVolume());
        Assertions.assertEquals(8566L, klineDto.getNumberOfTrades());
        Assertions.assertEquals(new BigDecimal("123.27824000"), klineDto.getTakerBuyBaseAssetVolume());
        Assertions.assertEquals(new BigDecimal("2045556.03773200"), klineDto.getTakerBuyQuoteAssetVolume());
        log.info("kline: {}", JsonUtil.toJson(klines));
    }

    @Test
    public void getSymbols() {
        List<SymbolDto> symbols = binanceExchange.getSymbols();
        log.info("symbols:{}", JsonUtil.toJson(symbols));
    }

    @Test
    public void getKline() {
        KlineParam param = new KlineParam();
        param.setSymbol("BTCUSDT");
        param.setInterval(KlineTypeEnum.FIVE_MINUTE.code);
        param.setStartTime(DateUtil.parseDateTime("2023-08-18 14:30:00"));
        param.setEndTime(DateUtil.parseDateTime("2023-08-18 14:40:00"));
        List<KlineDto> klines = binanceExchange.getKline(param);
        for (KlineDto klineDto: klines) {
            log.info("kline:{}", JsonUtil.toJson(klineDto));
        }
    }

    @Test
    public void webcosket() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        BinanceApiWebSocketClient webSocketClient = binanceExchange.getWebSocketClient();
        BinanceApiConfig.useTestnetStreaming = true;
        Closeable closeable = webSocketClient.onCandlestickEvent("ethbtc", CandlestickInterval.ONE_MINUTE, new BinanceApiCallback<CandlestickEvent>() {
            @Override
            public void onResponse(CandlestickEvent response) {
                log.info("response:{}", JsonUtil.toJson(response));
            }

            @Override
            public void onFailure(Throwable cause) {
                log.error("error", cause);
            }
        });
        try {
            Thread.sleep(TimeUnit.MINUTES.toMillis(2L));
            closeable.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        countDownLatch.await();
    }
}
