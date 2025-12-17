package cn.minglg.interview.ai.core.interview;

import cn.minglg.commons.model.task.TaskType;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:AiInterviewCoreServiceTest
 * Package:cn.minglg.interview.ai.core.interview
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/10
 * @Version 1.0
 */
@SpringBootTest
public class AiInterviewCoreServiceTest {

    @Autowired
    private ResumeDetailRepository resumeDetailRepository;
    @Autowired
    private Map<TaskType, PromptTemplate> systemPromptDynamicTemplate;

    @Test
    public void test1() {

    }

    @NotNull
    private Map<String, Object> getVariables(ResumeDetail resumeDetail) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("companyName", "小米科技");
        variables.put("totalRounds", 3);
        variables.put("candidateName", resumeDetail.getBasicInfo().getName());
        variables.put("jobTitle", "java开发工程师");
        variables.put("jobRequirements",
                """
                        1. 掌握Java语言开发技术，Java基础扎实，对多线程，IO，网络编程等掌握熟练。
                        2. 掌握常用的数据结构、算法和设计模式，具备良好的编程习惯；
                        3. 掌握MySQL、Redis、MQ等技术框架底层原理，熟练应用日常开发与问题诊断；
                        4. 具备分布式、高并发、高可用性等系统设计能力，并有大型应用系统开发经验；
                        5. 具备良好的问题分析和解决问题能力、具备良好沟通表达与团队协作能力，具备较强的责任心和团队合作精神；
                        6. 喜欢专研及尝试新的技术，具有良好的技术敏锐度，能从技术趋势和思路上影响技术团队；""");
        variables.put("resumeText", resumeDetail.getRawText());
        variables.put("techFocusAreas", "Java,数据库，高并发");
        return variables;
    }
}
