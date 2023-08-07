package cc.jooylife.meerkat.test;

import cc.jooylife.environment.EnvUtil;
import cc.jooylife.meerkat.util.JsonUtil;
import com.binance4j.core.dto.Candle;
import com.binance4j.core.dto.OrderBookEntry;
import com.binance4j.core.exception.ApiException;
import com.binance4j.market.client.MarketClient;
import com.binance4j.market.dto.*;
import com.binance4j.market.param.ExchangeInfoParams;
import com.binance4j.market.param.KlinesParams;
import com.binance4j.market.param.OrderBookParams;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class Binance4JTest {

    private final String apiKey = EnvUtil.getValue("meerkat.binance.api-key");

    private final String apiSecret = EnvUtil.getValue("meerkat.binance.api-secret");

    @Test
    void systemTimeTest() {
        MarketClient client = new MarketClient(apiKey, apiSecret);
        try {
            ServerTimeResponse sync = client.getServerTime().sync();
            log.info("abc:{}", sync.serverTime());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void exchangeTest() {
        MarketClient client = new MarketClient(apiKey, apiSecret);
        try {
            ExchangeInfoParams params = new ExchangeInfoParams();
            ExchangeInfo exchangeInfo = client.getExchangeInfo(params).sync();
            List<SymbolInfo> symbols = exchangeInfo.symbols();
            log.info("symbolsSize:{}", symbols.size());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    // 生成K线数据的函数测试
    @Test
    public void klinesTest() {
        MarketClient client = new MarketClient(apiKey, apiSecret);
        try {
            KlinesParams klinesParams = new KlinesParams("FILUSDT", "5m");
            List<Candle> klines = client.getKlines(klinesParams).sync();
            klines.forEach(kline -> {
                log.info("kline:{}", JsonUtil.toJson(kline));
            });
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    // Get a symbol's order book
    @Test
    public void orderBookTest() {
        MarketClient client = new MarketClient(apiKey, apiSecret);
        try {
            OrderBookParams orderBookParams = new OrderBookParams("BTCUSDT", OrderBookLimit.LIMIT_5);
            OrderBook orderBook = client.getOrderBook(orderBookParams).sync();
            List<OrderBookEntry> bids = orderBook.bids();
            for (OrderBookEntry bid : bids) {
                log.info("bid:{}", JsonUtil.toJson(bid));
            }
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
