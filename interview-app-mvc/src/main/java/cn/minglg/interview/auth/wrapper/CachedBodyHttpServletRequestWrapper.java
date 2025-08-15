package cn.minglg.interview.auth.wrapper;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ClassName:CachedBodyHttpServletRequestWrapper
 * Package:cn.minglg.interview.auth.wrapper
 * Description:把请求体（body）内容缓存到内存中，允许后续多次读取
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
public class CachedBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 原生的 HttpServletRequest.getInputStream() 只能读取一次，读完就没了。
     * 如果有多个 Filter 或 Controller 都需要读取 body（比如做参数校验、日志、认证等），
     * 就会报错或拿不到数据。通过包装，把 body 先读出来缓存，后续每次调用 getInputStream()
     * 或 getReader() 都能重新读取同样的数据。
     * <p>
     * 1.请求到达时，RequestBodyCacheFilter 用 CachedBodyHttpServletRequest 包装原始 request。
     * 2.第一次读取 body 时，CachedBodyHttpServletRequest 把 body 全部读到 byte[]。
     * 3.后续每次调用 getInputStream() 或 getReader()，都能基于缓存的 byte[] 返回新的流或 reader，内容和原始 body 完全一致。
     * 4.所有 filter/controller 都能多次读取 body，不会再有“流被消费”问题。
     */

    private final byte[] cachedBody;

    public CachedBodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = requestInputStream.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStreamWrapper(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
