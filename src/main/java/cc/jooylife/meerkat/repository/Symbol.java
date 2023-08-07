package cc.jooylife.meerkat.repository;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName symbol
 */
@TableName(value ="symbol")
@Data
public class Symbol extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Object id;

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
    private Integer icebergAllowed;

    /**
     * oco
     */
    private Integer ocoAllowed;

    /**
     * quote_order
     */
    private Integer quoteOrderQtyMarketAllowed;

    /**
     * 允许现货交易
     */
    private Integer spotTradingAllowed;

    /**
     * cancel replace allowed
     */
    private Integer cancelReplaceAllowed;

    /**
     * 允许追踪止损
     */
    private Integer allowTrailingStop;

    /**
     * 保证金交易
     */
    private Integer marginTradingAllowed;

    /**
     * 状态，TRADING-交易中
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}