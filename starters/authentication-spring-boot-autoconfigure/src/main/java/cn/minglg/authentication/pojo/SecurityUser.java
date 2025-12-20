package cn.minglg.authentication.pojo;

import cn.minglg.commons.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * ClassName:User
 * Package:cn.minglg.authentication.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/13
 * @Version 1.0
 */
@NoArgsConstructor
@JsonIgnoreProperties({"handler", "authorities"})
public class SecurityUser extends User implements UserDetails, Serializable {

    public SecurityUser(User user) {
        super(user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getCompany(),
                user.getRoles());
    }

    /**
     * 返回授予用户的权限。。
     *
     * @return 权限，按自然键排序
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        this.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString())));
        return authorities;
    }

    /**
     * 指示用户是启用还是禁用。已禁用的用户不能通过认证。
     * 如果用户已启用，则为true，否则为false
     */
    @Override
    public boolean isEnabled() {
        return "NORMAL".equalsIgnoreCase(this.getStatus().toString());
    }

}
