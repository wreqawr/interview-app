package cn.minglg.interview.interview.config;

import cn.minglg.interview.interview.converter.JobConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * ClassName:R2DbcConfig
 * Package:cn.minglg.interview.interview.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/31
 * @Version 1.0
 */
@Configuration
@EnableR2dbcRepositories
public class R2DbcConfig {
    /**
     * 创建R2dbc自定义转换器Bean
     * 该方法用于创建R2dbcCustomConversions类型的Bean，用于处理R2DBC数据库操作中的自定义类型转换。
     * 当容器中不存在R2dbcCustomConversions类型的Bean时，才会创建此Bean。
     *
     * @return R2dbcCustomConversions 返回配置了MySQL方言和JobConverter转换器的自定义转换器实例
     */
    @Bean
    @ConditionalOnMissingBean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        // 使用MySQL方言和自定义的JobConverter创建R2dbc自定义转换器
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, new JobConverter());
    }

}
