DROP TABLE IF EXISTS t_permission;
-- 权限表 (系统操作点)
CREATE TABLE IF NOT EXISTS t_permission
(
    permission_id   INT                 NOT NULL AUTO_INCREMENT COMMENT '权限id(自增主键)',
    permission_code VARCHAR(100) UNIQUE NOT NULL COMMENT '权限代码常量',
    description     VARCHAR(200)        NOT NULL COMMENT '权限描述',
    PRIMARY KEY (permission_id)
) COMMENT ='权限明细表';