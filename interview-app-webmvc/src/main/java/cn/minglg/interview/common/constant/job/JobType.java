package cn.minglg.interview.common.constant.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:JobType
 * Package:cn.minglg.interview.common.constant
 * Description:工作类型枚举
 *
 * @Author kfzx-minglg
 * @Create 2025/8/12
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum JobType {
    /**
     * 枚举值
     */
    FULL_TIME("全职"),
    PART_TIME("兼职"),
    INTERNSHIP("实习");

    private final String description;
}
