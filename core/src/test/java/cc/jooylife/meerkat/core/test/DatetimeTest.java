package cc.jooylife.meerkat.core.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author: wuhaiming
 * @Date: 2023/8/18 14:56
 */
@Slf4j
public class DatetimeTest {

    @Test
    public void test() {
        DateTime now = DateUtil.date();
        int minute = now.minute();
        int remainder = minute % 5;
        DateTime endDate = now.offset(DateField.MINUTE, -(remainder+1));
        endDate.setField(DateField.SECOND, 0);
        log.info("endDate:{}", endDate);
    }
}
