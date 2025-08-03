package cn.minglg.interview.common.aspects;

import cn.minglg.interview.common.annotation.AsyncTaskHandler;
import cn.minglg.interview.common.constant.TaskStatus;
import cn.minglg.interview.common.constant.TaskType;
import cn.minglg.interview.common.mapper.TaskMapper;
import cn.minglg.interview.common.pojo.Task;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * ClassName:AsyncTaskAspect
 * Package:cn.minglg.interview.aspects
 * Description:异步任务切面类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
@RequiredArgsConstructor
@Aspect
@Component
public class AsyncTaskAspect {
    private final TaskMapper taskMapper;

    /**
     * 给所有加了@AsyncTaskHandler注解的方法添加执行状态记录
     *
     * @param pjp     连接点（被增强的方法）
     * @param handler 注解对象
     * @return 执行结果
     * @throws Throwable 异常
     */
    @Around("@annotation(handler)")
    public Object handleAsyncTask(ProceedingJoinPoint pjp, AsyncTaskHandler handler) throws Throwable {
        // 第一步：获取初始化参数
        Object[] args = pjp.getArgs();
        Long userId = (Long) args[0];
        String taskId = (String) args[1];
        TaskType taskType = handler.taskType();
        TaskStatus taskStatus = TaskStatus.RUNNING;
        String methodName = pjp.getSignature().toLongString();
        String methodArgs = Arrays.toString(args);
        LocalDateTime startTime = LocalDateTime.now();
        Task task = Task.builder().userId(userId)
                .taskId(taskId)
                .taskType(taskType)
                .taskStatus(taskStatus)
                .methodName(methodName)
                .methodArgs(methodArgs)
                .startTime(startTime)
                .build();
        Object result = null;
        try {
            // 第二步：初始化任务状态表
            taskMapper.initTask(task);
            // 第三步：执行目标方法
            result = pjp.proceed();
            // 执行成功，更新状态
            task.setEndTime(LocalDateTime.now());
            task.setTaskStatus(TaskStatus.FINISHED);
        } catch (Throwable throwable) {
            task.setEndTime(LocalDateTime.now());
            task.setTaskStatus(TaskStatus.FAILED);
            task.setErrorMessage(throwable.getMessage());
        }
        taskMapper.updateTask(task);
        return result;

    }
}
