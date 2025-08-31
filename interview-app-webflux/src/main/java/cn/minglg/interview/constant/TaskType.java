package cn.minglg.interview.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:TaskType
 * Package:cn.minglg.interview.constant
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

    SKILL_EVALUATION("能力评估"),
    COMPREHENSIVE_ASSESSMENT("综合评估"),
    RESUME_METADATA_UPDATE("简历元数据更新"),

    TASK_RESULT_QUERY("任务执行结果查询"),

    MOCK_INTERVIEW("模拟面试"),
    MOCK_INTERVIEW_STOP("模拟面试结束"),

    GENERAL_CHAT("通用聊天"),


    OTHER("其它任务类型");


    private final String description;

    /**
     * 根据字符串查找对应的TaskType枚举值
     *
     * @param text         要查找的字符串
     * @param defaultValue 当找不到匹配的枚举值时返回的默认值
     * @return 匹配的TaskType枚举值，如果未找到则返回默认值
     */
    public static TaskType fromString(String text, TaskType defaultValue) {
        // 遍历所有TaskType枚举值进行匹配
        if (text != null) {
            for (TaskType s : TaskType.values()) {
                if (s.name().equalsIgnoreCase(text)) {
                    return s;
                }
            }
        }
        // 未找到匹配项时返回默认值
        return defaultValue;
    }

}
