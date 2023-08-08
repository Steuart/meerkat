package cc.jooylife.meerkat.test.exchange;

import cc.jooylife.meerkat.common.dto.KlineDto;
import cc.jooylife.meerkat.common.dto.SymbolDto;
import cc.jooylife.meerkat.common.param.KlineParam;
import cc.jooylife.meerkat.exchange.BinanceExchange;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class BinanceExchangeTest {

    @Autowired
    private BinanceExchange binanceExchange;


    @Test
    public void getKlineTest() {
        KlineParam param = new KlineParam();
        param.setSymbol("BTCUSDT");
        param.setInterval("5m");
        List<KlineDto> kline = binanceExchange.getKline(param);
        log.info("klineSize:{}", kline.size());
    }

    @Test
    public void getSymbols() {
        List<SymbolDto> symbols = binanceExchange.getSymbols();
        log.info("symbols:{}", symbols);
    }
}
