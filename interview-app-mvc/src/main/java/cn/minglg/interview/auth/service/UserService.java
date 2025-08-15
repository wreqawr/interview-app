package cn.minglg.interview.auth.service;

import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.response.R;
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
    R register(User user);
}
