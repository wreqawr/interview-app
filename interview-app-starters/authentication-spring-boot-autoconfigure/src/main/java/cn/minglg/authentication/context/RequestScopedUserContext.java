package cn.minglg.authentication.context;

import lombok.Data;
import org.springframework.web.context.annotation.RequestScope;

/**
 * ClassName:RequestScopedUserContext
 * Package:cn.minglg.authentication.context
 * Description:请求作用域的用户上下文
 *
 * @Author kfzx-minglg
 * @Create 2025/8/24
 * @Version 1.0
 */
@Data
@RequestScope
public class RequestScopedUserContext {
    private Long userId;
}
