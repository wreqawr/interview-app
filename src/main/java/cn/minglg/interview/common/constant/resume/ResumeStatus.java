package cn.minglg.interview.common.constant.resume;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:ResumeStatus
 * Package:cn.minglg.interview.common.constant
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ResumeStatus {
    /**
     * 枚举值
     */

    PARSING("解析中"),

    EFFECTIVE("生效中"),

    DELETED("已删除");

    private final String displayName;

}
