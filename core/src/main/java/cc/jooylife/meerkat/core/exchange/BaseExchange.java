package cc.jooylife.meerkat.core.exchange;


import cc.jooylife.meerkat.core.common.dto.KlineDto;
import cc.jooylife.meerkat.core.common.dto.SymbolDto;
import cc.jooylife.meerkat.core.common.param.KlineParam;

import java.util.List;

public abstract class BaseExchange {

    /**
     * 获取K线数据
     */
    public abstract List<KlineDto> getKline(KlineParam param);

    /**
     * 获取所有的交易对
     */
    public abstract List<SymbolDto> getSymbols();
}
