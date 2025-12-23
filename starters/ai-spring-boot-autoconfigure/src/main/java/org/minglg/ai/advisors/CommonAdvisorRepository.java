package org.minglg.ai.advisors;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

/**
 * ClassName:CommonAdvisorRepository
 * Package:cn.minglg.ai.advisors
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/27
 * @Version 1.0
 */
public interface CommonAdvisorRepository {
    /**
     * 获取advisor顺序
     *
     * @return advisor顺序
     */
    int getOrder();

    /**
     * 修改当前对话请求
     *
     * @param chatClientRequest 原始请求
     * @return 修改后的请求
     */
    ChatClientRequest modifyChatClientRequest(ChatClientRequest chatClientRequest);

    /**
     * 修改聊天客户端响应对象
     * 默认不做任何操作，后续加入项目有需求可重写此方法
     *
     * @param chatClientResponse 要修改的聊天客户端响应对象
     * @return 修改后的聊天客户端响应对象
     */
    default ChatClientResponse modifyChatClientResponse(ChatClientResponse chatClientResponse) {
        return chatClientResponse;
    }

    /**
     * 从上下文参数映射中获取指定键的值，如果键不存在或值为null则返回默认值
     * 该方法提供了类型安全的参数获取机制
     *
     * @param context 上下文参数映射，用于存储键值对参数
     * @param key 要获取值的键名
     * @param defaultValue 当键不存在、值为null或类型转换失败时返回的默认值
     * @param <T> 返回值的泛型类型
     * @return 从上下文中获取的值，如果获取失败则返回默认值
     */
    default <T> T getContextParam(Map<String, Object> context, String key, T defaultValue) {
        // 提前检查空值情况
        if (context == null || context.isEmpty() || !context.containsKey(key)) {
            return defaultValue;
        }

        Object value = context.get(key);

        // 尝试进行类型安全的转换
        try {
            @SuppressWarnings("unchecked")
            T castValue = (T) value;
            return castValue;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }


    /**
     * 获取默认的调度器实例
     *
     * @return 返回一个有界弹性调度器(Schedulers.boundedElastic ())，该调度器适用于I/O密集型任务，
     * 能够动态创建和销毁线程，同时限制最大线程数以避免资源耗尽
     */
    default Scheduler getScheduler() {
        return Schedulers.boundedElastic();
    }


}
