package cc.jooylife.meerkat.core.repository.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 
 * symbol
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="symbol")
@Data
public class Symbol extends BaseEntity implements Serializable {


    /**
     * 名字
     */
    private String name;

    /**
     * 报价资产
     */
    private String quoteAsset;

    /**
     * 基础资产
     */
    private String baseAsset;

    /**
     * iceberg
     */
    private Boolean icebergAllowed;

    /**
     * oco
     */
    private Boolean ocoAllowed;

    /**
     * quote_order
     */
    private Boolean quoteOrderQtyMarketAllowed;

    /**
     * 允许现货交易
     */
    private Boolean spotTradingAllowed;

    /**
     * cancel replace allowed
     */
    private Boolean cancelReplaceAllowed;

    /**
     * 允许追踪止损
     */
    private Boolean allowTrailingStop;

    /**
     * 保证金交易
     */
    private Boolean marginTradingAllowed;

    /**
     * 交易状态，TRADING-交易中
     * @see cc.jooylife.meerkat.core.common.enums.ExchangeStatusEnum
     */
    private String exchangeStatus;

    /**
     * 状态
     * @see cc.jooylife.meerkat.core.common.enums.SymbolStatusEnum
     */
    private String status;
}