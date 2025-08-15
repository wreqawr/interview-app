package cn.minglg.interview.auth.mapper;

import cn.minglg.interview.auth.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * ClassName:UserMapper
 * Package:cn.minglg.interview.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/13
 * @Version 1.0
 */
public interface UserMapper {
    /**
     * 根据用户名获取用户（包含公司、角色、权限）
     *
     * @param userName 用户名
     * @return 用户详细信息
     */
    User getUserWithDetailsByUserName(@Param("userName") String userName);

    /**
     * 获取用户基本信息
     *
     * @param userName 用户名
     * @return 用户基本信息
     */
    User getBasicUserByUserName(@Param("userName") String userName);

    /**
     * 添加用户
     *
     * @param user 用户信息
     */
    void addUser(@Param("user") User user);
}
