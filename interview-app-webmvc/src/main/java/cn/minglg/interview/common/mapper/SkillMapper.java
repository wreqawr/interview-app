package cn.minglg.interview.common.mapper;

import cn.minglg.authentication.pojo.Role;
import cn.minglg.interview.common.pojo.Skill;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

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
@Repository
public interface SkillMapper extends BaseMapper<Skill> {


    /**
     * 根据角色列表获取对应的技能列表
     *
     * @param roleList 角色列表，用于查询对应技能
     * @return 符合条件的技能列表
     */
    default List<Skill> getSkillByRoleId(List<Role> roleList) {
        // 构造查询条件，根据角色ID列表查询技能
        LambdaQueryWrapper<Skill> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Skill::getRoleId, roleList.stream().map(Role::getRoleId).toList());
        return selectList(wrapper);
    }
}
