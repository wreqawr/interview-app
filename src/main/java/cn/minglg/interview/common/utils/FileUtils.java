package cn.minglg.interview.common.utils;


import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * ClassName:FileReaderUtils
 * Package:cn.minglg.interview.common.utils
 * Description:文件读取工具类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
public class FileUtils {
    /**
     * 读取文件内容
     *
     * @param resourceLoader 资源加载器
     * @param file           文件
     * @return 文件内容
     */
    public static String readFileFromClassPath(ResourceLoader resourceLoader, String file) {
        file = file.startsWith("classpath:") ? file : "classpath:" + file;
        Resource resource = resourceLoader.getResource(file);
        try (InputStream is = resource.getInputStream()) {
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}
