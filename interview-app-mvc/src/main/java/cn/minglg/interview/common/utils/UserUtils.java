package cn.minglg.interview.common.utils;

import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.exception.UnKnowUserException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ClassName:UserUtils
 * Package:cn.minglg.interview.common.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/24
 * @Version 1.0
 */
public class UserUtils {
    /**
     * 获取当前登录用户对象
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new UnKnowUserException("未知用户！");
    }
}
