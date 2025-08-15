DROP TABLE IF EXISTS t_resume_metadata;
CREATE TABLE IF NOT EXISTS t_resume_metadata
(
    resume_id       VARCHAR(50) PRIMARY KEY COMMENT '文件ID',
    resume_title    VARCHAR(50) COMMENT '简历标题',
    view_count      INT COMMENT '查看次数',
    download_count  INT COMMENT '下载次数',
    rate            Decimal(5, 1) COMMENT '综合评分',
    user_id         BIGINT UNSIGNED                          NOT NULL COMMENT '用户ID',
    bucket_name     VARCHAR(63)                              NOT NULL COMMENT 'MinIO桶名称',
    object_name     VARCHAR(700)                             NOT NULL COMMENT 'MinIO对象名称',
    original_name   VARCHAR(255)                             NOT NULL COMMENT '原始文件名',
    file_size       BIGINT UNSIGNED                          NOT NULL COMMENT '文件大小（字节）',
    mime_type       VARCHAR(100)                             NOT NULL DEFAULT 'application/octet-stream' COMMENT '文件类型',
    preview_enabled BOOLEAN                                  NOT NULL DEFAULT FALSE COMMENT '是否支持预览',
    sha256          CHAR(64) COMMENT '文件哈希值（校验用）',
    status          ENUM ('PARSING', 'EFFECTIVE', 'DELETED') NOT NULL DEFAULT 'EFFECTIVE' COMMENT '文件状态',
    created_at      DATETIME                                          DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME                                          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_user_id (user_id),
    INDEX idx_bucket (bucket_name),
    UNIQUE INDEX udx_object (bucket_name, object_name) -- 确保存储位置唯一
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;