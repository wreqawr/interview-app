package cn.minglg.interview.common.mapper;

import cn.minglg.authentication.pojo.Role;
import cn.minglg.interview.common.pojo.Skill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName:SkillMapper
 * Package:cn.minglg.interview.common.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
public interface SkillMapper {

    /**
     * 根据角色id获取技能列表
     *
     * @param roleList 角色列表
     * @return 技能列表
     */
    List<Skill> getSkillByRoleId(@Param("roleList") List<Role> roleList);
}
