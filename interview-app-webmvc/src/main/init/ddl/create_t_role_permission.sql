DROP TABLE IF EXISTS t_role_permission;
-- 角色权限映射表 (RBAC核心)
CREATE TABLE IF NOT EXISTS t_role_permission
(
    role_id       INT NOT NULL COMMENT '角色id',
    permission_id INT NOT NULL COMMENT '权限id',
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES t_role (role_id),
    FOREIGN KEY (permission_id) REFERENCES t_permission (permission_id)
) COMMENT ='角色权限关系映射表';