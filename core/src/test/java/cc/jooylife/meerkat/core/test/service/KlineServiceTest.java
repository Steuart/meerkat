package cc.jooylife.meerkat.core.test.service;

import cc.jooylife.meerkat.core.service.KlineService;
import cc.jooylife.meerkat.core.test.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class KlineServiceTest {

    @Autowired
    private KlineService klineService;

    @Test
    public void testSyncKlines() {
        klineService.syncKline("BTCUSDT");
    }
}
