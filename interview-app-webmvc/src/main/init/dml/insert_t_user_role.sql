-- 分配角色
INSERT INTO t_user_role (user_id, role_id)
VALUES (1, 1), -- 管理员
       (2, 2), -- 求职者
       (3, 3), -- ROLE_HR
       (3, 2);-- 多角色用户(求职)