package cn.minglg.interview.common.constant.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author kfzx-minglg
 */
@Getter
@AllArgsConstructor
public enum UserRole {
    // 管理员
    ROLE_ADMIN("管理员"),
    // 求职者
    ROLE_JOB_SEEKER("求职者"),
    // ROLE_HR
    ROLE_HR("企业招聘方");

    private final String displayName;

}
