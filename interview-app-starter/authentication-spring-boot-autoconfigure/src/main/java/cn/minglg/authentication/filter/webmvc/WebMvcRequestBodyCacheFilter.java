package cn.minglg.authentication.filter.webmvc;

import cn.minglg.authentication.wrapper.CachedBodyHttpServletRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ClassName:WebMvcRequestBodyCacheFilter
 * Package:cn.minglg.authentication.filter
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
public class WebMvcRequestBodyCacheFilter extends OncePerRequestFilter {


    /**
     * 执行过滤器的内部逻辑，对请求进行处理
     *
     * @param request     HTTP请求对象
     * @param response    HTTP响应对象
     * @param filterChain 过滤器链对象
     * @throws ServletException Servlet异常
     * @throws IOException      IO异常
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
        // 对非文件上传请求进行请求体缓存包装处理
        CachedBodyHttpServletRequestWrapper cachedBodyHttpServletRequestWrapper = new CachedBodyHttpServletRequestWrapper(request);
        filterChain.doFilter(cachedBodyHttpServletRequestWrapper, response);
    }

}
