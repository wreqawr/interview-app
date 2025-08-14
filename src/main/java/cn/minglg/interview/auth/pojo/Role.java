package cn.minglg.interview.auth.pojo;

import cn.hutool.core.annotation.PropIgnore;
import cn.minglg.interview.common.constant.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName:Role
 * Package:cn.minglg.interview.pojo
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
    @PropIgnore
    private List<Permission> permissions;
}
