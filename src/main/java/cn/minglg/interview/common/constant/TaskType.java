package cn.minglg.interview.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:TaskType
 * Package:cn.minglg.interview.common.constant
 * Description:任务类型
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum TaskType {
    /**
     * 任务类型枚举
     */
    RESUME_SUMMARIZE("简历解析"),
    SKILL_EVALUATION("能力评估"),
    JOB_MATCH_ANALYSIS("岗位匹配度分析"),

    OTHER("其它任务类型");


    private final String description;
}
