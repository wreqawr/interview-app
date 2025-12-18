DROP TABLE IF EXISTS t_development_progress;
-- 开发进度管理表
CREATE TABLE IF NOT EXISTS t_development_progress
(
    progress_id        INT           NOT NULL AUTO_INCREMENT COMMENT '进度记录ID',
    module_name        VARCHAR(100)  NOT NULL COMMENT '功能模块名称',
    module_code        VARCHAR(50)   NOT NULL COMMENT '功能模块代码',
    role_type          VARCHAR(20)   NOT NULL COMMENT '角色类型(ROLE_ADMIN/ROLE_HR/ROLE_JOB_SEEKER)',
    priority           INT           NOT NULL DEFAULT 1 COMMENT '优先级(1-5,5最高)',
    status             VARCHAR(20)   NOT NULL DEFAULT 'pending' COMMENT '开发状态(pending/developing/testing/completed/paused)',
    frontend_progress  INT           NOT NULL DEFAULT 0 COMMENT '前端进度(0-100)',
    backend_progress   INT           NOT NULL DEFAULT 0 COMMENT '后端进度(0-100)',
    database_progress  INT           NOT NULL DEFAULT 0 COMMENT '数据库进度(0-100)',
    overall_progress   INT           NOT NULL DEFAULT 0 COMMENT '整体进度(0-100)',
    estimated_hours    DECIMAL(5, 2) NOT NULL DEFAULT 0 COMMENT '预估工时(小时)',
    actual_hours       DECIMAL(5, 2) NOT NULL DEFAULT 0 COMMENT '实际工时(小时)',
    planned_start_date DATE          NULL COMMENT '计划开始日期',
    planned_end_date   DATE          NULL COMMENT '计划完成日期',
    actual_start_date  DATE          NULL COMMENT '实际开始日期',
    actual_end_date    DATE          NULL COMMENT '实际完成日期',
    developer          VARCHAR(50)   NOT NULL DEFAULT 'minglg' COMMENT '开发人员',
    description        TEXT          NULL COMMENT '功能描述',
    requirements       TEXT          NULL COMMENT '需求说明',
    technical_notes    TEXT          NULL COMMENT '技术备注',
    dependencies       VARCHAR(500)  NULL COMMENT '依赖模块',
    created_time       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (progress_id),
    INDEX idx_module_code (module_code),
    INDEX idx_role_type (role_type),
    INDEX idx_status (status),
    INDEX idx_priority (priority)
) COMMENT = '开发进度管理表';
