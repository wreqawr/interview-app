package cn.minglg.commons.model.user;

import lombok.Data;

/**
 * ClassName:Permission
 * Package:cn.minglg.authentication.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/12
 * @Version 1.0
 */
@Data
public class Permission {
    private Long permissionId;
    private String permissionCode;
    private String description;
}
