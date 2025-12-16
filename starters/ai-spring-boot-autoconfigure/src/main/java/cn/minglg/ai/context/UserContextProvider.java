package cn.minglg.ai.context;

/**
 * ClassName:UserContextProvider
 * Package:cn.minglg.ai.context
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/22
 * @Version 1.0
 */
@FunctionalInterface
public interface UserContextProvider {
    /**
     * 获取用户id
     *
     * @return 用户id
     */
    Long getUserId();
}
