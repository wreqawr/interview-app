package cn.minglg.interview.auth.pojo;

import cn.hutool.core.annotation.PropIgnore;
import lombok.Data;

/**
 * ClassName:Company
 * Package:cn.minglg.interview.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/12
 * @Version 1.0
 */
@Data
public class Company {
    @PropIgnore
    private Long companyId;
    private String companyName;
}
