package cc.jooylife.meerkat.exchange;

import com.binance4j.market.client.MarketClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BinanceExchange extends BaseExchange {

    @Value("${exchange.binance.api-key}")
    private String apiKey;

    @Value("${exchange.binance.secret-key}")
    private String apiSecret;

    private volatile MarketClient marketClient;

    /**
     * 使用单例获取 MarketClient
     */
    public MarketClient getMarketClient() {
        if (marketClient == null) {
            synchronized (BinanceExchange.class) {
                if (marketClient == null) {
                    marketClient = new MarketClient(apiKey, apiSecret);
                }
            }
        }
        return marketClient;
    }
}
