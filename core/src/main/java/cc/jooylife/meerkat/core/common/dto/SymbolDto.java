package cc.jooylife.meerkat.core.common.dto;

import lombok.Data;

@Data
public class SymbolDto {
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
     * 状态，TRADING-交易中
     */
    private String status;
}
