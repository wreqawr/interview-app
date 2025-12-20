package cn.minglg.commons.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName:User
 * Package:cn.minglg.authentication.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/13
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"handler", "authorities"})
public class User implements Serializable {
    /**
     * 核心字段
     */
    private Long userId;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private String email;
    private UserStatus status;
    @JsonIgnore
    private LocalDateTime createdAt;

    /**
     * 复合字段
     */
    private Company company;
    private List<Role> roles;

    @Getter
    @AllArgsConstructor
    public enum UserStatus {
        /**
         * 枚举值
         */
        NORMAL("正常"),
        CANCELLED("已注销"),
        LOCKED("已锁定");

        private final String displayName;

    }

}
