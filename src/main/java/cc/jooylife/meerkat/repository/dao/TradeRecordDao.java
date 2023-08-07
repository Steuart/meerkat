package cc.jooylife.meerkat.repository.dao;

import cc.jooylife.meerkat.repository.entity.TradeRecord;
import cc.jooylife.meerkat.repository.mapper.TradeRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @Author: wuhaiming
 * @Date: 2023/8/7 17:51
 */
@Repository
public class TradeRecordDao extends ServiceImpl<TradeRecordMapper, TradeRecord> {
}
