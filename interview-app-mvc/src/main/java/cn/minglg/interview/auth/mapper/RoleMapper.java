package cn.minglg.interview.auth.mapper;

import cn.minglg.interview.auth.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName:RoleMapper
 * Package:cn.minglg.interview.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
public interface RoleMapper {
    /**
     * 根据用户ID获取关联的角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRoleListByUserId(@Param("userId") Long userId);

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<Role> getAllRoleList();
}