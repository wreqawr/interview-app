package cn.minglg.interview.resume.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ClassName:ResumeParserService
 * Package:cn.minglg.interview.resume.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/26
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ResumeParserService {
    private final AutoDetectParser autoDetectParser;
    /**
     * 预编译正则表达式
     */
    private static final Pattern EXCESS_SPACES_PATTERN = Pattern.compile("[ \t]{2,}");

    /**
     * 简历解析核心方法
     *
     * @param is 文件输入流
     * @return 解析结果
     * @throws Exception 异常
     */
    public String parseResume(InputStream is) throws Exception {
        BodyContentHandler bodyContentHandler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        autoDetectParser.parse(is, bodyContentHandler, metadata, parseContext);
        return cleanContent(bodyContentHandler.toString());
    }

    /**
     * 内容清理主方法
     *
     * @param content 需要处理的内容
     * @return 处理之后的结果
     */
    private String cleanContent(String content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }

        // 第一步：统一换行符
        String normalized = normalizeLineEndings(content);

        // 第二步：删除所有空白行（包括仅包含空格的行）
        String removed = removeAllBlankLines(normalized);

        // 第三步：移除行首行尾空白
        String trimmed = trimLineEnds(removed);

        // 第四步：缩减内部连续空白
        return reduceInternalSpaces(trimmed);
    }

    /**
     * 统一换行符
     *
     * @param content 需处理的内容
     * @return 处理后的结果
     */
    private String normalizeLineEndings(String content) {
        return content.replace("\r\n", "\n")
                .replace('\r', '\n');
    }

    /**
     * 删除所有空白行（包括仅包含空格的行）
     *
     * @param content 需处理的内容
     * @return 处理后的结果
     */
    private String removeAllBlankLines(String content) {
        // 分割为行数组，过滤掉所有空白行，再重新连接
        return Arrays.stream(content.split("\n"))
                .filter(line -> !line.trim().isEmpty())
                .collect(Collectors.joining("\n"));
    }

    /**
     * 移除行首行尾空白
     *
     * @param content 需处理的内容
     * @return 处理后的结果
     */
    private String trimLineEnds(String content) {
        return Arrays.stream(content.split("\n"))
                .map(String::trim)
                .collect(Collectors.joining("\n"));
    }

    /**
     * 缩减内部连续空白
     *
     * @param content 需处理的内容
     * @return 处理后的结果
     */
    private String reduceInternalSpaces(String content) {
        return EXCESS_SPACES_PATTERN
                .matcher(content)
                .replaceAll(" ");
    }
}
