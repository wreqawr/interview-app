package cn.minglg.interview.aop;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * ClassName:AsyncTest
 * Package:cn.minglg.interview.aop
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/6
 * @Version 1.0
 */
@Component
public class AsyncTest {

    @SneakyThrows
    @Async
    @CustomHandler
    public void asyncMethod() {
        System.out.println("*****开始执行异步方法");
        Thread.sleep(3000);
        System.out.println("*****异步方法执行结束");
    }
}
