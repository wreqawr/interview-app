package cn.minglg.resume;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ClassName:ResumeApplication
 * Package:cn.minggl.resume
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/18
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableAsync
@EnableFeignClients
@MapperScan(value = {
        "cn.minglg.resume.mapper",
        "cn.minglg.commons.mapper"
})
@SpringBootApplication(scanBasePackages = {
        "cn.minglg.resume",
        "cn.minglg.commons"
})
public class ResumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication.class, args);
    }
}
