package cn.minglg.interview.common.pojo;

import cn.minglg.commons.model.task.TaskStatus;
import cn.minglg.commons.model.task.TaskType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ClassName:Task
 * Package:cn.minglg.interview.common.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_task")
public class Task implements Serializable {
    /**
     * id(自增主键)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 唯一任务ID
     */
    private String taskId;

    /**
     * 任务类型
     */
    private TaskType taskType;

    /**
     * 任务状态
     */
    private TaskStatus taskStatus;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数
     */
    private String methodArgs;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 错误详情
     */
    private String errorMessage;

    @Serial
    private static final long serialVersionUID = 1L;
}
