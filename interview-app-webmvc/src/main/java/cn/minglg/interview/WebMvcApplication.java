package cn.minglg.interview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ClassName:WebMvcApplication
 * Package:cn.minglg.interview
 * Description:
 *
 * @author kfzx-minglg
 * Create 2025/6/13
 * @version 1.0
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value = {
        "cn.minglg.interview.user.mapper",
        "cn.minglg.interview.resume.mapper",
        "cn.minglg.interview.common.mapper",
        "cn.minglg.interview.job.mapper"})
@EnableAsync
@EnableDiscoveryClient
public class WebMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMvcApplication.class, args);
    }

}
