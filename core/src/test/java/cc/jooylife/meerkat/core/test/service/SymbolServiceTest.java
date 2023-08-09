package cc.jooylife.meerkat.core.test.service;

import cc.jooylife.meerkat.core.service.SymbolService;
import cc.jooylife.meerkat.core.test.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class SymbolServiceTest {

    @Autowired
    private SymbolService symbolService;

    @Test
    public void testSyncSymbol() {
        symbolService.syncSymbol();
    }
}
