package cc.jooylife.meerkat.repository.dao;

import cc.jooylife.meerkat.repository.entity.Kline;
import cc.jooylife.meerkat.repository.mapper.KlineMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class KlineDao extends ServiceImpl<KlineMapper, Kline> {


    /**
     * 创建表
     * @param tableName 表名
     */
    public void createTable(String tableName) {
        baseMapper.createTable(tableName);
    }
}
