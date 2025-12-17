package cn.minglg.commons.model.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

/**
 * ClassName:Role
 * Package:cn.minglg.authentication.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/12
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private Integer roleId;
    private UserRole roleName;
    private String description;
    @JsonIgnore
    private List<Permission> permissions;

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
}
