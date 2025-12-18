DROP TABLE IF EXISTS t_role;
-- 角色表 (Spring Security规范ROLE_前缀)
CREATE TABLE IF NOT EXISTS t_role
(
    role_id     INT                NOT NULL AUTO_INCREMENT COMMENT '角色id(自增主键)',
    role_name   VARCHAR(20) UNIQUE NOT NULL COMMENT '角色常量值',
    description VARCHAR(50)        NOT NULL COMMENT '角色描述',
    PRIMARY KEY (role_id)
) COMMENT ='系统角色表(字典表)';