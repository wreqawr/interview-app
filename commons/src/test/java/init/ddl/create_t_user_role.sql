DROP TABLE IF EXISTS t_user_role;
-- 用户角色映射表
CREATE TABLE IF NOT EXISTS t_user_role
(
    user_id BIGINT NOT NULL COMMENT '用户id',
    role_id INT    NOT NULL COMMENT '角色id',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES t_user (user_id),
    FOREIGN KEY (role_id) REFERENCES t_role (role_id)
) COMMENT ='用户角色关系映射表';