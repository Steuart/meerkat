package cc.jooylife.meerkat.test.dao;


import cc.jooylife.meerkat.repository.dao.SymbolDao;
import cc.jooylife.meerkat.repository.entity.Symbol;
import cc.jooylife.meerkat.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class SymbolDaoTest {

    @Autowired
    private SymbolDao symbolDao;

    @Test
    public void getSymbol() {
        Symbol symbol = symbolDao.getById(1L);
        log.info("symbol:{}", JsonUtil.toJson(symbol));
    }
}
