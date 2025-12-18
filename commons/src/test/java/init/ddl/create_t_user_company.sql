DROP TABLE IF EXISTS t_user_company;
-- 用户单位映射表
CREATE TABLE IF NOT EXISTS t_user_company
(
    user_id    BIGINT NOT NULL COMMENT '用户id',
    company_id BIGINT NOT NULL COMMENT '公司id',
    PRIMARY KEY (user_id, company_id),
    FOREIGN KEY (user_id) REFERENCES t_user (user_id),
    FOREIGN KEY (company_id) REFERENCES t_company (company_id)
) COMMENT ='用户单位映射表';