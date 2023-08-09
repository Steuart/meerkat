package cc.jooylife.meerkat.core.exchange;

import cc.jooylife.meerkat.core.common.dto.KlineDto;
import cc.jooylife.meerkat.core.common.dto.SymbolDto;
import cc.jooylife.meerkat.core.common.param.KlineParam;
import cc.jooylife.meerkat.core.util.JsonUtil;
import com.binance4j.core.Request;
import com.binance4j.core.dto.Candle;
import com.binance4j.core.exception.ApiException;
import com.binance4j.core.param.TimeFrame;
import com.binance4j.market.client.MarketClient;
import com.binance4j.market.dto.ExchangeInfo;
import com.binance4j.market.dto.SymbolInfo;
import com.binance4j.market.param.KlinesParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    @Override
    public List<KlineDto> getKline(KlineParam param) {
        KlinesParams klinesParams = new KlinesParams(param.getSymbol(), param.getInterval());
        Long startTime = null;
        if (param.getStartTime() != null) {
            startTime = param.getStartTime().getTime();
        }
        Long endTime = null;
        if (param.getEndTime() != null) {
            endTime = param.getEndTime().getTime();
        }
        Integer limit = null;
        if (param.getLimit() != null) {
            limit = param.getLimit();
        }
        TimeFrame timeFrame = new TimeFrame(startTime, endTime, limit);
        MarketClient client = getMarketClient();
        Request<List<Candle>> klines = client.getKlines(klinesParams, timeFrame);
        List<KlineDto> result = new ArrayList<>();
        try {
            List<Candle> candles = klines.sync();
            candles.forEach(candle -> {
                KlineDto klineDto = klineConvert(candle);
                result.add(klineDto);
            });
        } catch (ApiException e) {
            log.error("Get kline error, param:{}", JsonUtil.toJson(param), e);
        }
        return result;
    }

    @Override
    public List<SymbolDto> getSymbols() {
        MarketClient client = getMarketClient();
        Request<ExchangeInfo> exchangeInfo = client.getExchangeInfo();
        List<SymbolDto> result = new ArrayList<>();
        try {
            ExchangeInfo exchange = exchangeInfo.sync();
            List<SymbolInfo> symbols = exchange.symbols();
            symbols.forEach(symbolInfo -> {
                SymbolDto symbolDto = new SymbolDto();
                BeanUtils.copyProperties(symbolInfo, symbolDto);
                symbolDto.setName(symbolInfo.symbol());
                result.add(symbolDto);
            });
        } catch (ApiException e) {
            log.error("Get symbols error", e);
        }
        return result;
    }

    /**
     * 转换KlineDto
     * @param candle Candle
     */
    private KlineDto klineConvert(Candle candle) {
        KlineDto klineDto = new KlineDto();
        klineDto.setOpenTime(new Date(candle.openTime()));
        klineDto.setOpen(new BigDecimal(candle.open()));
        klineDto.setHigh(new BigDecimal(candle.high()));
        klineDto.setLow(new BigDecimal(candle.low()));
        klineDto.setClose(new BigDecimal(candle.close()));
        klineDto.setVolume(new BigDecimal(candle.volume()));
        klineDto.setCloseTime(new Date(candle.closeTime()));
        klineDto.setQuJoteAssetVolume(new BigDecimal(candle.quoteAssetVolume()));
        klineDto.setNumberOfTrades(candle.numberOfTrades());
        klineDto.setTakerBuyBaseAssetVolume(new BigDecimal(candle.takerBuyBaseAssetVolume()));
        klineDto.setTakerBuyQuoteAssetVolume(new BigDecimal(candle.takerBuyBaseAssetVolume()));
        return klineDto;
    }
}
