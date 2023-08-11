package cc.jooylife.meerkat.core.exchange;

import cc.jooylife.meerkat.core.common.dto.KlineDto;
import cc.jooylife.meerkat.core.common.dto.SymbolDto;
import cc.jooylife.meerkat.core.common.param.KlineParam;
import cc.jooylife.meerkat.core.util.JsonUtil;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class BinanceExchange extends BaseExchange {

    @Value("${exchange.binance.api-key}")
    private String apiKey;

    @Value("${exchange.binance.api-secret}")
    private String apiSecret;

    @Value("${exchange.binance.base-domain:}")
    private String baseDomain;


    private volatile BinanceApiRestClient marketClient;

    @PostConstruct
    public void init() {
        if (!ObjectUtils.isEmpty(baseDomain)) {
            BinanceApiConfig.setBaseDomain(baseDomain);
        }
    }

    /**
     * 使用单例获取 MarketClient
     */
    public BinanceApiRestClient getMarketClient() {
        if (marketClient == null) {
            synchronized (BinanceExchange.class) {
                if (marketClient == null) {
                    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, apiSecret);
                    marketClient = factory.newRestClient();
                }
            }
        }
        return marketClient;
    }

    @Override
    public List<KlineDto> getKline(KlineParam param) {
        Long startTime = null;
        if (param.getStartTime() != null) {
            startTime = param.getStartTime().getTime();
        }
        Long endTime = null;
        if (param.getEndTime() != null) {
            endTime = param.getEndTime().getTime();
        }
        Integer limit = 500;
        if (param.getLimit() != null) {
            limit = param.getLimit();
        }
        BinanceApiRestClient client = getMarketClient();
        CandlestickInterval candlestickInterval = getCandlestickInterval(param.getInterval());
        List<KlineDto> result = new ArrayList<>();
        try {
            List<Candlestick> candlestickBars = client.getCandlestickBars(param.getSymbol(), candlestickInterval, limit, startTime, endTime);
            candlestickBars.forEach(candle -> {
                KlineDto klineDto = klineConvert(candle);
                result.add(klineDto);
            });
        } catch (Exception e) {
            log.error("Get kline error, param:{}", JsonUtil.toJson(param), e);
        }
        return result;
    }

    @Override
    public List<SymbolDto> getSymbols() {
        BinanceApiRestClient client = getMarketClient();
        ExchangeInfo exchangeInfo = client.getExchangeInfo();
        List<SymbolDto> result = new ArrayList<>();
        try {
            List<SymbolInfo> symbols = exchangeInfo.getSymbols();
            symbols.forEach(symbolInfo -> {
                SymbolDto symbolDto = new SymbolDto();
                BeanUtils.copyProperties(symbolInfo, symbolDto);
                symbolDto.setName(symbolInfo.getSymbol());
                result.add(symbolDto);
            });
        } catch (Exception e) {
            log.error("Get symbols error", e);
        }
        return result;
    }

    /**
     * 转换KlineDto
     * @param candle Candle
     */
    private KlineDto klineConvert(Candlestick candle) {
        KlineDto klineDto = new KlineDto();
        klineDto.setOpenTime(new Date(candle.getOpenTime()));
        klineDto.setOpen(new BigDecimal(candle.getOpen()));
        klineDto.setHigh(new BigDecimal(candle.getHigh()));
        klineDto.setLow(new BigDecimal(candle.getLow()));
        klineDto.setClose(new BigDecimal(candle.getClose()));
        klineDto.setVolume(new BigDecimal(candle.getVolume()));
        klineDto.setCloseTime(new Date(candle.getCloseTime()));
        klineDto.setQuJoteAssetVolume(new BigDecimal(candle.getQuoteAssetVolume()));
        klineDto.setNumberOfTrades(candle.getNumberOfTrades());
        klineDto.setTakerBuyBaseAssetVolume(new BigDecimal(candle.getTakerBuyBaseAssetVolume()));
        klineDto.setTakerBuyQuoteAssetVolume(new BigDecimal(candle.getTakerBuyQuoteAssetVolume()));
        return klineDto;
    }

    private CandlestickInterval getCandlestickInterval(String interval) {
        for (CandlestickInterval candlestickInterval : CandlestickInterval.values()) {
            if (candlestickInterval.getIntervalId().equals(interval)) {
                return candlestickInterval;
            }
        }
        return CandlestickInterval.FIVE_MINUTES;
    }
}
