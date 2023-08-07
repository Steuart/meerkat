package cc.jooylife.meerkat.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName kline
 */
@TableName(value ="kline")
@Data
public class Kline extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Object id;

    /**
     * 
     */
    private Date openTime;

    /**
     * 
     */
    private BigDecimal open;

    /**
     * 
     */
    private BigDecimal high;

    /**
     * 
     */
    private BigDecimal low;

    /**
     * 
     */
    private BigDecimal close;

    /**
     * 
     */
    private BigDecimal volume;

    /**
     * 
     */
    private Date closeTime;

    /**
     * 
     */
    private BigDecimal quJoteAssetVolume;

    /**
     * 
     */
    private Integer numberOfTrades;

    /**
     * 
     */
    private BigDecimal takerBuyBaseAssetVolume;

    /**
     * 
     */
    private BigDecimal takerBuyQuoteAssetVolume;

    /**
     * 
     */
    private String interval;

    /**
     * 
     */
    private Date createDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}