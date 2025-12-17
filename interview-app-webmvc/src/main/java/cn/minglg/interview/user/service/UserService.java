package cn.minglg.interview.user.service;

import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.user.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * ClassName:UserService
 * Package:cn.minglg.interview.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/13
 * @Version 1.0
 */
public interface UserService extends UserDetailsService {

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 执行结果
     */
    GenericResponse<?> register(User user);
}
