package cn.minglg.interview.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName:Skill
 * Package:cn.minglg.interview.common.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@Data
@TableName("t_skill")
public class Skill {
    @TableId(value = "skill_id", type = IdType.AUTO)
    private Integer skillId;
    private Integer roleId;
    private String skillDescription;
}
