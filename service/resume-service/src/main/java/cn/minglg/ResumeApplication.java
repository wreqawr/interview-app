package cn.minglg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName:ResumeApplication
 * Package:cn.minggl.resume
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/18
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = {
        "cn.minglg.resume.mapper",
        "cn.minglg.commons.mapper"
})
public class ResumeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResumeApplication.class, args);
    }
}
