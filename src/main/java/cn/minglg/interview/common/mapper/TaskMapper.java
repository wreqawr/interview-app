package cn.minglg.interview.common.mapper;

import cn.minglg.interview.common.pojo.Task;
import org.apache.ibatis.annotations.Param;

/**
 * @author kfzx-minglg
 */
public interface TaskMapper {
    /**
     * 初始化任务
     *
     * @param task 任务信息
     */
    void initTask(@Param("task") Task task);

    /**
     * 更新任务信息
     *
     * @param task 任务信息
     */
    void updateTask(@Param("task") Task task);

    /**
     * 根据任务id查询状态
     *
     * @param task 任务信息
     * @return 任务信息
     */
    Task getTask(@Param("task") Task task);
}