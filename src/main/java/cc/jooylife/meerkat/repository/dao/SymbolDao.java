package cc.jooylife.meerkat.repository.dao;

import cc.jooylife.meerkat.repository.entity.Symbol;
import cc.jooylife.meerkat.repository.mapper.SymbolMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class SymbolDao extends ServiceImpl<SymbolMapper, Symbol> {

}
