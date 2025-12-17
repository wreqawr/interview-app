package cn.minglg.interview.test;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * ClassName:TestController
 * Package:cn.minglg.interview.test
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/16
 * @Version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ChatClient chatClient;

    @RequestMapping(value = "/flux")
    public Flux<String> testFlux() {
        return chatClient
                .prompt()
                // 本次对话的系统提示词
                .user("你好，我是张三，请介绍一下你自己！")
                .stream()
                .content();
    }
}
