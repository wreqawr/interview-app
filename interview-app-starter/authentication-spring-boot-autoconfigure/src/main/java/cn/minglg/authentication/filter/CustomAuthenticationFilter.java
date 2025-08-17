package cn.minglg.authentication.filter;

import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.utils.RsaUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.KeyPair;
import java.util.Map;

/**
 * ClassName:CustomAuthenticationFilter
 * Package:cn.minglg.authentication.filter
 * Description:自定义用户名密码过滤器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/10
 * @Version 1.0
 */
@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String REQUEST_METHOD = "POST";
    private final KeyPair keyPair;
    private final WebMvcSecurityProperties securityProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter(WebMvcSecurityProperties securityProperties, KeyPair keyPair, AuthenticationManager authenticationManager) {
        this.keyPair = keyPair;
        this.securityProperties = securityProperties;
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(securityProperties.getLoginUri());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!REQUEST_METHOD.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 从JSON请求体中解析用户名和密码
        Map<?, ?> authData;
        try {
            authData = objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException("Failed to parse authentication request body");
        }
        String username = (String) authData.get("username");
        username = (username != null) ? username.trim() : "";
        String encryptMessage = (String) authData.get("password");
        encryptMessage = (encryptMessage != null) ? encryptMessage : "";
        String decryptPassword;
        try {
            decryptPassword = RsaUtils.decrypt(encryptMessage, keyPair.getPrivate(), securityProperties.getRequestTimeoutSeconds());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                decryptPassword);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
