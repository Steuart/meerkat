package cc.jooylife.meerkat.core.test.exchange;

import cc.jooylife.meerkat.core.common.dto.KlineDto;
import cc.jooylife.meerkat.core.common.enums.KlineTypeEnum;
import cc.jooylife.meerkat.core.common.param.KlineParam;
import cc.jooylife.meerkat.core.exchange.BinanceExchange;
import cc.jooylife.meerkat.core.test.Application;
import cc.jooylife.meerkat.core.util.JsonUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        List<KlineDto> kline = binanceExchange.getKline(param);
        log.info("kline: {}", JsonUtil.toJson(kline));
    }
}
