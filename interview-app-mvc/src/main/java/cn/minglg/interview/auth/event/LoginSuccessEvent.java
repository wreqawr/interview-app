package cn.minglg.interview.auth.event;

import cn.minglg.interview.auth.pojo.User;
import org.springframework.context.ApplicationEvent;

/**
 * ClassName:LoginSuccessEvent
 * Package:cn.minglg.interview.event
 * Description:登录成功事件
 *
 * @Author kfzx-minglg
 * @Create 2025/6/18
 * @Version 1.0
 */
public class LoginSuccessEvent extends ApplicationEvent {
    /**
     * 登录成功事件
     *
     * @param event 事件携带的信息
     */
    public LoginSuccessEvent(User event) {
        super(event);
    }
}
