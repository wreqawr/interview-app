package cn.minglg.interview.auth.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;

/**
 * ClassName:CachedBodyServletInputStreamWrapper
 * Package:cn.minglg.interview.auth.wrapper
 * Description:内存中的 byte[] 数据包装成一个可以多次读取的输入流
 *
 * @Author kfzx-minglg
 * @Create 2025/7/20
 * @Version 1.0
 */
public class CachedBodyServletInputStreamWrapper extends ServletInputStream {
    /**
     * Servlet 规范要求 getInputStream() 返回的是 ServletInputStream 类型，
     * 不能直接用 ByteArrayInputStream。这个类就是把 byte[] 包装成 ServletInputStream，
     * 并实现了相关方法。
     */
    private final ByteArrayInputStream inputStream;

    public CachedBodyServletInputStreamWrapper(byte[] cachedBody) {
        this.inputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public int read() {
        return inputStream.read();
    }

    @Override
    public boolean isFinished() {
        return inputStream.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
    }
}
