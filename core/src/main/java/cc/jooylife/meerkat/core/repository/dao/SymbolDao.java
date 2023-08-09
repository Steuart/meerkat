package cc.jooylife.meerkat.core.repository.dao;

import cc.jooylife.meerkat.core.repository.entity.Symbol;
import cc.jooylife.meerkat.core.repository.mapper.SymbolMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class SymbolDao extends ServiceImpl<SymbolMapper, Symbol> {

}
