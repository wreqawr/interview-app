package cn.minglg.authentication.filter.webmvc;

import cn.minglg.authentication.constant.response.ResponseCode;
import cn.minglg.authentication.pojo.User;
import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.response.R;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.authentication.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ClassName:WebMvcJwtTokenFilter
 * Package:cn.minglg.authentication.filter
 * Description:JWT验证过滤器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
@AllArgsConstructor
public class WebMvcJwtTokenFilter extends OncePerRequestFilter {
    private final WebMvcSecurityProperties securityProperties;
    private final StringRedisTemplate redisTemplate;


    /**
     * 执行过滤器的内部逻辑，用于JWT token验证和用户认证
     *
     * @param request     HTTP请求对象
     * @param response    HTTP响应对象
     * @param filterChain 过滤器链对象
     * @throws ServletException Servlet异常
     * @throws IOException      IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        R checkResult = R.builder().code(ResponseCode.JWT_VERIFY_FAIL.getCode()).message("请先登录！").build();
        // 绿色通道或者预检请求直接放行
        RequestMatcher requestMatcher = this.securityProperties.getWhiteListPatternsAsRequestMatcher();
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        if ((requestMatcher != null && requestMatcher.matches(request))) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        try {
            // 解析token，获取userId，和redis比较
            String secretKey = securityProperties.getJwtSecretKey();
            User user = JwtUtils.verifyJwt(token, secretKey);
            String authKey = securityProperties.getAuthKeyPrefix() + ":" + user.getUserId();
            String redisToken = redisTemplate.opsForValue().get(authKey);
            // 验证通过放行
            if (redisToken != null && redisToken.equals(token)) {
                // 要在spring security的上下文中放置一个认证对象。
                // 这样的话，spring security在执行后续的Filter的时候，
                // 才知道这个人是登录了的。
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
            } else {
                response.getWriter().write(JsonUtils.toJsonStr(checkResult));
            }
        } catch (Exception e) {
            response.getWriter().write(JsonUtils.toJsonStr(checkResult));
        }
    }

}
