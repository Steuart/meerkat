package cc.jooylife.meerkat.exchange;

import cc.jooylife.meerkat.common.dto.KlineDto;
import cc.jooylife.meerkat.common.dto.SymbolDto;
import cc.jooylife.meerkat.common.param.KlineParam;

import java.util.List;

public abstract class BaseExchange {

    /**
     * 获取K线数据
     * @param symbol 交易对
     * @param interval K线周期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return K线数据
     */
    public abstract List<KlineDto> getKline(KlineParam param);

    /**
     * 获取所有的交易对
     */
    public abstract List<SymbolDto> getSymbols();
}
