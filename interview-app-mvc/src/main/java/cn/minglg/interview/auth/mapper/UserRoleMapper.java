package cn.minglg.interview.auth.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * ClassName:UserRoleMapper
 * Package:cn.minglg.interview.auth.mapper
 * Description:用户角色映射
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
public interface UserRoleMapper {
    /**
     * 添加用户-角色关系映射
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    void addUserRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);
}
