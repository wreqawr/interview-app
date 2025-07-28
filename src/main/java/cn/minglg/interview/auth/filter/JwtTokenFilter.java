package cn.minglg.interview.auth.filter;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyPair;

/**
 * ClassName:JwtTokenFilter
 * Package:cn.minglg.interview.filter
 * Description:JWT验证过滤器
 *
 * @Author kfzx-minglg
 * @Create 2025/7/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final KeyPair keyPair;
    private final StringRedisTemplate redisTemplate;
    private final GlobalProperties globalProperties;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        R checkResult = R.builder().code(ResponseCode.JWT_VERIFY_FAIL.getCode()).message("请先登录！").build();
        // 绿色通道或者预检请求直接放行
        if (this.globalProperties.getWhiteListPatternsAsRequestMatcher().matches(request)
                || "OPTIONS".equalsIgnoreCase(request.getMethod())
        ) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        try {
            // 解析token，获取userId，和redis比较
            User user = JwtUtils.verifyJwt(token, keyPair);
            String authKey = globalProperties.getAuth().getAuthKeyPrefix() + ":" + user.getUserId();
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
                response.getWriter().write(JSONUtil.toJsonStr(checkResult));
            }
        } catch (Exception e) {
            response.getWriter().write(JSONUtil.toJsonStr(checkResult));
        }
    }
}
