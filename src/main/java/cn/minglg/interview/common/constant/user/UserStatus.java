package cn.minglg.interview.common.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author kfzx-minglg
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    /**
     * 枚举值
     */
    NORMAL("正常"),
    CANCELLED("已注销"),
    LOCKED("已锁定");

    private final String displayName;

}
