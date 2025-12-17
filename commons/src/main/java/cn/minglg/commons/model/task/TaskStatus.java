package cn.minglg.commons.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:TaskStatus
 * Package:cn.minglg.interview.common.constant
 * Description:任务状态
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum TaskStatus {
    /**
     * 任务状态枚举值
     */
    PENDING("等待执行"),
    RUNNING("执行中"),
    FINISHED("执行结束"),
    FAILED("执行失败");


    private final String description;
}
