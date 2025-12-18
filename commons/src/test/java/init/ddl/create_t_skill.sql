DROP TABLE IF EXISTS t_skill;
CREATE TABLE IF NOT EXISTS t_skill
(
    skill_id          INT                NOT NULL AUTO_INCREMENT COMMENT '技能id(自增主键)',
    role_id           INT                NOT NULL COMMENT '角色id',
    skill_description VARCHAR(50) UNIQUE NOT NULL COMMENT '技能描述',
    PRIMARY KEY (skill_id)
) COMMENT ='ai技能地图表';