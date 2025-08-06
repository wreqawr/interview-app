package cn.minglg.interview.common.utils;

import cn.minglg.interview.common.mapper.TaskMapper;
import cn.minglg.interview.common.pojo.Task;

import java.util.UUID;

/**
 * ClassName:TaskUtils
 * Package:cn.minglg.interview.common.utils
 * Description:任务管理工具类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
public class TaskUtils {
    /**
     * 根据用户id和任务id查询任务信息
     *
     * @param taskMapper taskMapper
     * @param userId     用户id
     * @param taskId     任务id
     * @return 任务信息
     */
    public static Task queryTaskByUserIdAndTaskId(TaskMapper taskMapper, Long userId, String taskId) {
        Task task = Task.builder()
                .userId(userId)
                .taskId(taskId).build();
        return taskMapper.getTask(task);
    }

    /**
     * 生成taskId
     *
     * @return 任务id
     */
    public static String generateTaskId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
