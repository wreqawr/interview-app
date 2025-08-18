package cn.minglg.interview.common.constant.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:EducationLevel
 * Package:cn.minglg.interview.common.constant.job
 * Description:学历要求枚举
 *
 * @Author kfzx-minglg
 * @Create 2025/8/12
 * @Version 1.0
 */

@Getter
@AllArgsConstructor
public enum EducationLevel {
    /**
     * 枚举值
     */
    HIGH_SCHOOL("高中"),
    BACHELOR("本科"),
    POSTGRADUATE("研究生"),
    PHD("博士");

    private final String description;

}
