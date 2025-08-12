package cn.minglg.interview.common.constant.job;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:JobStatus
 * Package:cn.minglg.interview.common.constant.job
 * Description:岗位状态枚举
 *
 * @Author kfzx-minglg
 * @Create 2025/8/12
 * @Version 1.0
 */

@Getter
@AllArgsConstructor
public enum JobStatus {
    /**
     * 枚举值
     */
    DRAFT("草稿"),
    PUBLISHED("已发布"),
    CLOSED("已关闭"),
    EXPIRED("已过期");

    private final String description;

}
