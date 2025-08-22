package cn.minglg.authentication.utils;

import cn.minglg.authentication.exception.UnKnowUserException;
import cn.minglg.authentication.pojo.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * ClassName:UserUtils
 * Package:cn.minglg.authentication.utils
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
