DROP TABLE IF EXISTS t_company;
-- 企业信息表
CREATE TABLE IF NOT EXISTS t_company
(
    company_id   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '企业ID',
    company_name VARCHAR(100) NOT NULL COMMENT '企业名称',
    PRIMARY KEY (company_id),
    UNIQUE KEY uk_company_name (company_name) COMMENT '企业名称唯一索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='企业信息表';