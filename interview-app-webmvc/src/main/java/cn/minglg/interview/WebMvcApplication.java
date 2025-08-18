package cn.minglg.interview;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

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
@MapperScan(value = {"cn.minglg.interview.user.mapper", "cn.minglg.interview.resume.mapper", "cn.minglg.interview.common.mapper"})
@EnableAsync
public class WebMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMvcApplication.class, args);
    }

}
