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
    RESUME_SUMMARIZE("简历结构化"),
    RESUME_ANALYZE("简历分析"),
    SKILL_EVALUATION("能力评估"),
    COMPREHENSIVE_ASSESSMENT("综合评估"),
    RESUME_METADATA_UPDATE("简历元数据更新"),

    TASK_RESULT_QUERY("任务执行结果查询"),

    MOCK_INTERVIEW("模拟面试"),

    OTHER("其它任务类型");


    private final String description;
    }
