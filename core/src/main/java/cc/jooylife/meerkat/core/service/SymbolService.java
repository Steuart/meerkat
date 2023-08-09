package cc.jooylife.meerkat.core.service;

import cc.jooylife.meerkat.core.common.dto.SymbolDto;
import cc.jooylife.meerkat.core.exchange.BinanceExchange;
import cc.jooylife.meerkat.core.repository.dao.SymbolDao;
import cc.jooylife.meerkat.core.repository.entity.Symbol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SymbolService {

    private final SymbolDao symbolDao;

    private final BinanceExchange binanceExchange;

    @Autowired
    public SymbolService(SymbolDao symbolDao, BinanceExchange binanceExchange) {
        this.symbolDao = symbolDao;
        this.binanceExchange = binanceExchange;
    }

    /**
     * 同步交易对
     */
    public void syncSymbol() {
        List<SymbolDto> symbolDtos = binanceExchange.getSymbols();
        List<Symbol> symbols = symbolDao.list();
        Map<String, Symbol> symbolMap = symbols.stream().collect(Collectors.toMap(Symbol::getName, symbol -> symbol));
        List<Symbol> newSymbols = new ArrayList<>();
        for (SymbolDto symbolDto: symbolDtos) {
            if (!"USDT".equals(symbolDto.getQuoteAsset())) {
                continue;
            }
            String name = symbolDto.getName();
            Symbol symbol = symbolMap.get(name);
            if (symbol == null) {
                symbol = new Symbol();
            }
            BeanUtils.copyProperties(symbolDto, symbol);
            symbol.setUpdateDate(new Date());
            newSymbols.add(symbol);
        }
        log.info("sync symbols, size: {}", newSymbols.size());
        symbolDao.saveOrUpdateBatch(newSymbols);
    }
}
