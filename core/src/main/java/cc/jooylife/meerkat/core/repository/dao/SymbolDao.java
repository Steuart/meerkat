package cc.jooylife.meerkat.core.repository.dao;

import cc.jooylife.meerkat.core.repository.entity.Symbol;
import cc.jooylife.meerkat.core.repository.mapper.SymbolMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SymbolDao extends ServiceImpl<SymbolMapper, Symbol> {

    /**
     * 根据状态查询
     * @param status 状态
     * @return 交易对列表
     */
    public List<Symbol> listByStatus(String status) {
        LambdaQueryWrapper<Symbol> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Symbol::getStatus, status);
        return list(queryWrapper);
    }
}
