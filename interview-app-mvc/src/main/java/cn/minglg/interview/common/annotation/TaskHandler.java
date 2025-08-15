package cn.minglg.interview.common.annotation;


import cn.minglg.interview.common.constant.task.TaskType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kfzx-minglg
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskHandler {
    @AliasFor("taskType")
    TaskType value() default TaskType.OTHER;

    // 任务类型
    @AliasFor("value")
    TaskType taskType() default TaskType.OTHER;

    // 最大重试次数
    int maxRetries() default 3;

    // 超时时间（秒）
    long timeout() default 10 * 60;

}
