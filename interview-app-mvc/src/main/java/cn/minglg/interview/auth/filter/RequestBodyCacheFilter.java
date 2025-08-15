package cn.minglg.interview.auth.filter;

import cn.minglg.interview.auth.wrapper.CachedBodyHttpServletRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ClassName:RequestBodyCacheFilter
 * Package:cn.minglg.interview.auth.filter
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
@Component
public class RequestBodyCacheFilter extends OncePerRequestFilter {

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
        String contentType = request.getContentType();
        // 1. 放行 multipart/form-data（文件上传）请求
        String contentTypePrefix = "multipart/";
        if (contentType != null && contentType.toLowerCase().startsWith(contentTypePrefix)) {
            filterChain.doFilter(request, response);
            return;
        }
        CachedBodyHttpServletRequestWrapper cachedBodyHttpServletRequestWrapper = new CachedBodyHttpServletRequestWrapper(request);
        filterChain.doFilter(cachedBodyHttpServletRequestWrapper, response);
    }
}
