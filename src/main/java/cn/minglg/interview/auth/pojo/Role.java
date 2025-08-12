package cn.minglg.interview.auth.pojo;

import cn.hutool.core.annotation.PropIgnore;
import cn.minglg.interview.common.constant.user.UserRole;
import lombok.Data;

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
public class Role {
    private Integer roleId;
    private UserRole roleName;
    private String description;
    @PropIgnore
    private List<Permission> permissions;
}
