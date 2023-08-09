package cc.jooylife.meerkat.core.common.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class KlineDto {
    
    private Date openTime;
    
    private BigDecimal open;

     
    private BigDecimal high;

     
    private BigDecimal low;

     
    private BigDecimal close;

     
    private BigDecimal volume;

     
    private Date closeTime;

     
    private BigDecimal quJoteAssetVolume;

     
    private Long numberOfTrades;

     
    private BigDecimal takerBuyBaseAssetVolume;

     
    private BigDecimal takerBuyQuoteAssetVolume;
}
