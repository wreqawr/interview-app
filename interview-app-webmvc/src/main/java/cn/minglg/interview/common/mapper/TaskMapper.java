package cn.minglg.interview.common.mapper;

import cn.minglg.interview.common.pojo.Task;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author kfzx-minglg
 */
@Repository
public interface TaskMapper extends BaseMapper<Task> {
    /**
     * 初始化任务
     *
     * @param task 任务信息
     */
    default void initTask(Task task) {
        insert(task);
    }

    /**
     * 更新任务信息
     *
     * @param task 任务信息
     */
    default void updateTask(Task task) {
        LambdaUpdateWrapper<Task> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Task::getTaskStatus, task.getTaskStatus())
                .set(Task::getEndTime, task.getEndTime())
                .set(Task::getErrorMessage, task.getErrorMessage())
                .eq(Task::getTaskId, task.getTaskId())
                .eq(Task::getUserId, task.getUserId());
        // 执行更新
        update(wrapper);
    }

    /**
     * 根据任务id查询状态
     *
     * @param task 任务信息
     * @return 任务信息
     */
    default Task getTask(Task task) {
        return selectOne(new LambdaQueryWrapper<Task>()
                .eq(Task::getTaskId, task.getTaskId())
                .eq(Task::getUserId, task.getUserId()));
    }
}