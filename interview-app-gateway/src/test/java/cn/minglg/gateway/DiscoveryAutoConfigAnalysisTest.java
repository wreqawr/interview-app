package cn.minglg.gateway;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ClassName:DiscoveryAutoConfigAnalysisTest
 * Package:cn.minglg.gateway
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/10
 * @Version 1.0
 */
public class DiscoveryAutoConfigAnalysisTest {
    // 测试1：验证默认行为
    @Test
    public void testDefaultBehavior() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration.class
                ))
                .withUserConfiguration(TestConfig.class)
                .withPropertyValues(
                        "spring.cloud.discovery.enabled=true"
                )
                .run(context -> {
                    // 验证条件注解
                    assertThat(context)
                            .hasSingleBean(org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration.class);
                });
    }

    // 测试2：手动禁用
    @Test
    public void testDisabled() {
        new ApplicationContextRunner()
                .withPropertyValues(
                        "spring.cloud.discovery.enabled=false"
                )
                .run(context -> {
                    assertThat(context)
                            .doesNotHaveBean(DiscoveryClient.class);
                });
    }

    @Configuration
    static class TestConfig {
        // 测试配置
    }

    // 实际运行完整测试
    @Nested
    @SpringBootTest
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class FullTest {

        @Autowired(required = false)
        private DiscoveryClient discoveryClient;

        @Test
        public void analyzeDiscoveryClient() {
            if (discoveryClient == null) {
                System.out.println("No DiscoveryClient bean found.");
                System.out.println("Check if:");
                System.out.println("1. spring.cloud.discovery.enabled=true");
                System.out.println("2. Nacos dependencies are in classpath");
                System.out.println("3. Nacos server is available");
            } else {
                System.out.println("DiscoveryClient type: " +
                        discoveryClient.getClass().getName());
                System.out.println("Services: " +
                        discoveryClient.getServices());
            }
        }
    }
}
