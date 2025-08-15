package cn.minglg.interview.auth.mapper;

import cn.minglg.interview.auth.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName:PermissionMapper
 * Package:cn.minglg.interview.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
public interface PermissionMapper {
    /**
     * 根据角色ID获取权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getPermissionListByRoleId(@Param("roleId") Long roleId);
}
