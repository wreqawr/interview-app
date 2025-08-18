INSERT INTO t_user (username, password, nickname, email, status, created_at)
VALUES ('zhangsan', '$2a$10$NE7Tu2Z/a0oB50f2Ybm0aO/Mh.BzrU1AB1/y0MQbftz9jMDSsmlRm', '张三', 'zhangsan@qq.com',
        'NORMAL', CURRENT_TIMESTAMP),
       ('Tom', '$2a$10$vNDivz/HBD.RDzVsqNpZvOxDkYddJIg3kmZU7OhBX0gt.w/y7hEoy', '汤姆', 'Tom@qq.com',
        'NORMAL', CURRENT_TIMESTAMP),
       ('Jerry', '$2a$10$cPXnDkoG6Jq/xlUlU6hlvOCN3TyCgFUisTqWrOF8/VYd5YDJKEXMO', '杰瑞', 'Jerry@qq.com',
        'NORMAL', CURRENT_TIMESTAMP);
