DROP TABLE IF EXISTS t_user;
-- 用户表 (核心身份信息)
CREATE TABLE IF NOT EXISTS t_user
(
    user_id    BIGINT                               NOT NULL AUTO_INCREMENT COMMENT '用户id(自增主键)',
    username   VARCHAR(50) UNIQUE                   NOT NULL COMMENT '唯一用户名',
    password   VARCHAR(100)                         NOT NULL COMMENT '加密密码',
    nickname   VARCHAR(100)                         NOT NULL COMMENT '用户昵称',
    email      VARCHAR(100)                         NOT NULL COMMENT '邮箱',
    status     ENUM ('LOCKED','CANCELLED','NORMAL') NOT NULL DEFAULT 'NORMAL' COMMENT '账户状态(已锁定/已注销/正常)',
    created_at DATETIME                                      DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    PRIMARY KEY (user_id)
) COMMENT ='用户基础表(核心身份信息)';