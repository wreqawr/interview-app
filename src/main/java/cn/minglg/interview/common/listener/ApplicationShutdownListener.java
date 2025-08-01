package cn.minglg.interview.common.listener;

import cn.minglg.interview.common.properties.GlobalProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName:ApplicationShutdownListener
 * Package:cn.minglg.interview.auth.listener
 * Description:监听springboot项目停止
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private final StringRedisTemplate redisTemplate;
    private final GlobalProperties globalProperties;


    /**
     * 处理应用程序事件。
     * 当应用程序关闭时，清除redis中所有用户登录信息、验证码信息
     *
     * @param event 要响应的事件
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        String authKeyPrefix = globalProperties.getAuth().getAuthKeyPrefix();
        String captchaKeyPrefix = globalProperties.getCaptcha().getRedisKeyPrefix();
        String roleRedisKeyPrefix = globalProperties.getRegister().getRoleRedisKeyPrefix();
        this.deleteKeysByPrefix(authKeyPrefix);
        this.deleteKeysByPrefix(captchaKeyPrefix);
        this.deleteKeysByPrefix(roleRedisKeyPrefix);
    }

    /**
     * 删除所有以指定前缀开头的key
     *
     * @param prefix key前缀（例如：user:security:login）
     */
    public void deleteKeysByPrefix(String prefix) {
        // 构造匹配模式（注意：Redis的模式匹配需要包含冒号等特殊字符，直接使用前缀即可）
        ScanOptions scanOptions = ScanOptions.scanOptions().match(prefix + "*").count(100).build();

        // 使用游标迭代
        Cursor<String> cursor = redisTemplate.scan(scanOptions);
        while (cursor.hasNext()) {
            String key = cursor.next();
            // 逐个删除
            redisTemplate.delete(key);
        }
        cursor.close();
    }
}
