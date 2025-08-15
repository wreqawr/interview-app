package cn.minglg.interview.auth.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long companyId;
    private String companyName;
}
