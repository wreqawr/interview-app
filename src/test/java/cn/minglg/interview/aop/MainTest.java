package cn.minglg.interview.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ClassName:MainTest
 * Package:cn.minglg.interview.aop
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/6
 * @Version 1.0
 */
@SpringBootTest
@EnableAsync()
public class MainTest {
    @Autowired
    private AsyncTest asyncTest;
    @Test
    public void test1(){
        System.out.println("*****开始执行其他逻辑代码");
        asyncTest.asyncMethod();
        System.out.println("*****其他逻辑代码执行结束");
    }
}
