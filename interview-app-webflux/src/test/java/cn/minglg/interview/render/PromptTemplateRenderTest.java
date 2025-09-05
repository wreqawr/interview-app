package cn.minglg.interview.render;

import cn.minglg.commons.constant.task.TaskType;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * ClassName:PromptTemplateRenderTest
 * Package:cn.minglg.interview.render
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/9/5
 * @Version 1.0
 */
@SpringBootTest
public class PromptTemplateRenderTest {
    @Autowired
    private Map<TaskType, PromptTemplate> templateMap;
    @Test
    public void test1() {
        Map<String, Object> params = Map.of();
        PromptTemplate promptTemplate = templateMap.get(TaskType.MOCK_INTERVIEW);
        System.out.println(promptTemplate);
        String rendered = promptTemplate.render(params);
        System.out.println(rendered);
    }
}
