package cn.minglg.interview.common.constant.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:JobLevel
 * Package:cn.minglg.interview.common.constant.job
 * Description:岗位级别枚举
 *
 * @Author kfzx-minglg
 * @Create 2025/8/12
 * @Version 1.0
 */

@AllArgsConstructor
@Getter
public enum JobLevel {
    /**
     * 枚举值
     */
    JUNIOR("初级"),
    MIDDLE("中级"),
    SENIOR("高级"),
    EXPERT("专家");
    private final String description;
}
