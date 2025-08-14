package cn.minglg.interview.ai.service;

import cn.minglg.interview.auth.pojo.Role;
import cn.minglg.interview.common.mapper.SkillMapper;
import cn.minglg.interview.common.pojo.Skill;
import cn.minglg.interview.common.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName:ToolService
 * Package:cn.minglg.interview.ai.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ToolService {
    private final SkillMapper skillMapper;

    /**
     * 获取当前用户角色的技能列表
     *
     * @return 技能描述字符串列表，如果当前角色没有技能则返回null
     */
    @Tool(description = "能力地图，你会什么，有哪些功能，help")
    public List<String> getSkillMap() {
        // 获取当前用户角色信息
        List<Role> roleList;
        try {
            roleList = UserUtils.getCurrentUser().getRoles();
        } catch (Exception e) {
            roleList = List.of(Role.builder().roleId(-1).build());
        }
        // 根据当前用户角色ID查询mysql中保存的技能列表（不能跨权限查询）
        List<Skill> skillList = skillMapper.getSkillByRoleId(roleList);
        // 如果技能列表为空，返回null
        if (skillList == null || skillList.isEmpty()) {
            return List.of("我还什么都不会呢");
        }
        // 将技能对象列表转换为技能描述字符串列表
        return skillList.stream().map(Skill::getSkillDescription).toList();
    }

}
