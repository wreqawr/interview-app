package cn.minglg.interview;

import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * ClassName:CommonTest
 * Package:cn.minglg.interview
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/22
 * @Version 1.0
 */
public class CommonTest {

    @Test
    public void test1() {
        int totalLength = 20;
        long current = System.currentTimeMillis();
        String s = String.valueOf(current);
        String padded = "0".repeat(Math.max(0, totalLength - s.length())) + s;
        System.out.println(padded);
        System.out.println(padded.length());
    }

    @Test
    public void test2() {
        Path uploads = Paths.get(System.getProperty("java.io.tmpdir"), "resumeUploads");
        System.out.println(uploads);
    }

    @Test
    public void test3() {
        Map<String, ? extends Serializable> message = Map.of("原始文件名", "123",
                "存储文件名", "456",
                "文件类型", Objects.requireNonNull("AAA"),
                "文件大小", 999);
        System.out.println(JSONUtil.toJsonStr(message));
    }

    @SneakyThrows
    @Test
    public void test4() {
        String code = "AbstractUserDetailsAuthenticationProvider.badCredentials";
        String basename = "org.springframework.security.messages";
        Locale locale = Locale.CHINESE;
//        System.out.println(locale);

        Class<ResourceBundleMessageSource> clazz = ResourceBundleMessageSource.class;
        ResourceBundleMessageSource obj = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getDeclaredMethod("getResourceBundle", String.class, Locale.class);
        method.setAccessible(true);
        ResourceBundle bundle = (ResourceBundle) method.invoke(obj, basename, locale);
        String result = bundle.getString(code);
        System.out.println(result);

    }
}
