package cc.jooylife.meerkat.collector.config;

import cc.jooylife.meerkat.core.common.Constants;
import cc.jooylife.meerkat.core.util.KlineUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor interceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor tableNameInterceptor = new DynamicTableNameInnerInterceptor();
        tableNameInterceptor.setTableNameHandler((sql, tableName) -> {
            if (tableName.equals(Constants.KLINE_TABLE_NAME_PREFIX)) {
                return tableName + KlineUtil.getSymbol();
            }
            return tableName;
        });
        interceptor.addInnerInterceptor(tableNameInterceptor);
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
