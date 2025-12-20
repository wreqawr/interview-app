package cn.minglg.commons.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * ClassName:Company
 * Package:cn.minglg.authentication.pojo
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
