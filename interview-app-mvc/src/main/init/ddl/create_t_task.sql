DROP TABLE IF EXISTS t_task;
CREATE TABLE IF NOT EXISTS t_task
(
    id            BIGINT                                           NOT NULL AUTO_INCREMENT COMMENT 'id(自增主键)',
    user_id       BIGINT                                           NOT NULL COMMENT '用户id',
    task_id       VARCHAR(50) COMMENT '任务ID',
    task_type     VARCHAR(50)                                      NOT NULL COMMENT '任务类型',
    task_status   ENUM ('PENDING', 'RUNNING', 'FINISHED','FAILED') NOT NULL DEFAULT 'PENDING' NOT NULL COMMENT '任务状态',
    method_name   TEXT COMMENT '方法名',
    method_args   TEXT COMMENT '方法参数',
    start_time    DATETIME                                                  DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time      DATETIME                                                  DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
    error_message TEXT                                             NULL COMMENT '错误详情',

    PRIMARY KEY (id),
    INDEX idx_task (task_id),
    UNIQUE INDEX udx_object (user_id, task_id) -- 确保用户任务唯一
) COMMENT ='任务状态表'
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_unicode_ci;