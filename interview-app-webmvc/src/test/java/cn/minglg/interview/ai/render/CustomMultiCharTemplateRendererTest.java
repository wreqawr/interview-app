package cn.minglg.interview.ai.render;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:CustomMultiCharTemplateRendererTest
 * Package:cn.minglg.interview.ai.render
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/10
 * @Version 1.0
 */
@SpringBootTest
public class CustomMultiCharTemplateRendererTest {
    @Autowired
    private PromptTemplate promptTemplate;

    @TestConfiguration
    static class TestConfig {
        private final TemplateRenderer templateRenderer = CustomMultiCharTemplateRenderer.builder()
                //.startDelimiter("$#{")
                //.endDelimiter("}")
                .build();

        @Bean("testPromptTemplate")
        public PromptTemplate promptTemplate() {
            Resource resource = new ClassPathResource("/prompt/test/dynamic.st");
            return PromptTemplate.builder()
                    .resource(resource)
                    .renderer(templateRenderer)
                    .build();
        }
    }

    @Test
    public void testPromptTemplate() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "张三");
        variables.put("country", "中国");
        variables.put("questionCount", 5);
        String rendered = promptTemplate.render(variables);
        System.out.println(rendered);
    }
}
