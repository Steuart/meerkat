package cc.jooylife.meerkat.test.dao;

import cc.jooylife.meerkat.repository.dao.KlineDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KlineDaoTest {

    @Autowired
    private KlineDao klineDao;

    @Test
    public void testCreateTable() {
        klineDao.createTable("kline_filusdt");
    }
}
