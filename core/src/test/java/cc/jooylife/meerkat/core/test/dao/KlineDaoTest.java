package cc.jooylife.meerkat.core.test.dao;

import cc.jooylife.meerkat.core.common.enums.ExchangeStatusEnum;
import cc.jooylife.meerkat.core.repository.dao.KlineDao;
import cc.jooylife.meerkat.core.repository.dao.SymbolDao;
import cc.jooylife.meerkat.core.repository.entity.Symbol;
import cc.jooylife.meerkat.core.test.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = Application.class)
public class KlineDaoTest {

    @Autowired
    private KlineDao klineDao;

    @Autowired
    private SymbolDao symbolDao;

    @Test
    public void createTable() {
        klineDao.createTable("BTCUSDT");
    }

    @Test
    public void dropTables() {
        List<Symbol> symbols = symbolDao.listByStatus(ExchangeStatusEnum.TRADING.code);
        for (Symbol symbol: symbols) {
            klineDao.dropTable(symbol.getName());
        }
    }
}
