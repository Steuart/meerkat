package cc.jooylife.meerkat.repository.entity;

import cc.jooylife.meerkat.repository.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @TableName trade_record
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="trade_record")
@Data
public class TradeRecord extends BaseEntity implements Serializable {

    /**
     * 标的
     */
    private String symbol;

    /**
     * 交易订单id
     */
    private String orderId;

    /**
     * 订单时间
     */
    private Date orderDate;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 交易时间
     */
    private Date tradeDate;

    /**
     * 交易价格
     */
    private BigDecimal tradePrice;

    /**
     * 交易数量
     */
    private BigDecimal tradeQty;

    /**
     * 交易类型，buy-买入，sell-卖出
     */
    private String tradeType;

    /**
     * 是否主动卖出
     */
    private Integer buyerMaker;

    /**
     * 是否为最优撮合单
     */
    private Integer bestMatch;

    /**
     * 类型，simulation-模拟，reality-实盘
     */
    private String type;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}