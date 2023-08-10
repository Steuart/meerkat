package cc.jooylife.meerkat.core.repository.dao;


import cc.jooylife.meerkat.core.repository.entity.Kline;
import cc.jooylife.meerkat.core.repository.mapper.KlineMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Repository
public class KlineDao extends ServiceImpl<KlineMapper, Kline> {

    private static final String tablePrefix = "kline_";

    private static final ThreadLocal<String> SYMBOL_TABLE_NAME = new ThreadLocal<>();

    /**
     * 创建表
     */
    public void createTable(String symbol) {
        if (ObjectUtils.isEmpty(symbol)) {
            return;
        }
        String tableName = tablePrefix + symbol.toLowerCase();
        baseMapper.createTable(tableName);
    }

    /**
     * 删除表
     */
    public void dropTable(String symbol) {
        if (ObjectUtils.isEmpty(symbol)) {
            return;
        }
        String tableName = tablePrefix + symbol.toLowerCase();
        baseMapper.dropTable(tableName);
    }

    /**
     * 获取最新的数据
     */
    public Kline getLatestKline(String symbol) {
        setSymbolTableName(symbol.toLowerCase());
        LambdaQueryWrapper<Kline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Kline::getOpenTime);
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }


    public void deleteByOpenTime(String symbol, Date startTime, Date endTime, String type) {
        setSymbolTableName(symbol.toLowerCase());
        LambdaQueryWrapper<Kline> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(Kline::getOpenTime, startTime);
        queryWrapper.ge(Kline::getOpenTime, endTime);
        queryWrapper.eq(Kline::getType, type);
        remove(queryWrapper);
    }

    public static void setSymbolTableName(String symbol) {
        SYMBOL_TABLE_NAME.set(tablePrefix + symbol);
    }

    public static String getSymbolTableName() {
        return SYMBOL_TABLE_NAME.get();
    }
}
