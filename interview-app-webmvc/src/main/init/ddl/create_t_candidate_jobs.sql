DROP TABLE IF EXISTS t_candidate_jobs;
-- 求职者-岗位映射表
CREATE TABLE IF NOT EXISTS t_candidate_jobs
(
    candidate_id BIGINT NOT NULL COMMENT '求职者id',
    job_id       INT    NOT NULL COMMENT '岗位id',
    PRIMARY KEY (candidate_id, job_id)
) COMMENT ='求职者-岗位映射表';
