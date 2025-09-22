DROP TABLE IF EXISTS t_interview_record;
-- 求职者-岗位映射表
CREATE TABLE IF NOT EXISTS t_interview_record
(
    candidate_id BIGINT      NOT NULL COMMENT '求职者id',
    job_id       INT         NOT NULL COMMENT '岗位id',
    status       VARCHAR(20) NOT NULL DEFAULT 'post' COMMENT '状态(post/interviewing/hired/rejected)',
    PRIMARY KEY (candidate_id, job_id)
) COMMENT ='求职者-岗位映射表';
