package cn.minglg.interview.common.context;

import cn.minglg.ai.context.UserContextProvider;
import cn.minglg.authentication.utils.UserUtils;

/**
 * ClassName:SecurityUserContext
 * Package:cn.minglg.interview.ai.context
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/22
 * @Version 1.0
 */
public class SecurityUserContext implements UserContextProvider {
    /**
     * 获取用户id
     *
     * @return 用户id
     */
    @Override
    public Long getUserId() {
        return UserUtils.getCurrentUser().getUserId();
    }
}
