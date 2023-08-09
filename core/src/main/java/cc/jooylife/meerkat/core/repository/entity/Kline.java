package cc.jooylife.meerkat.core.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @TableName kline
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="kline")
@Data
public class Kline extends BaseEntity implements Serializable {

    private Date openTime;

    private BigDecimal open;

    private BigDecimal high;

    private BigDecimal low;

    private BigDecimal close;

    private BigDecimal volume;


    private Date closeTime;


    private BigDecimal quJoteAssetVolume;


    private Integer numberOfTrades;


    private BigDecimal takerBuyBaseAssetVolume;


    private BigDecimal takerBuyQuoteAssetVolume;


    private String interval;
}