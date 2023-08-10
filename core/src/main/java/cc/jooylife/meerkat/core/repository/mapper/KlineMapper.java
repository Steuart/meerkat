package cc.jooylife.meerkat.core.repository.mapper;

import cc.jooylife.meerkat.core.repository.entity.Kline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author haiming
* @description 针对表【kline】的数据库操作Mapper
* @createDate 2023-08-07 19:39:36
* @Entity cc.jooylife.meerkat.repository.Kline
*/
public interface KlineMapper extends BaseMapper<Kline> {


    void createTable(String tableName);


    void dropTable(String tableName);
}
