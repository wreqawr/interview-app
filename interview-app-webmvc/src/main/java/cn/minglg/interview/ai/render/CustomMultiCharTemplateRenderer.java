package cn.minglg.interview.ai.render;

import lombok.Builder;
import lombok.Data;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName:CustomMultiCharTemplateRenderer
 * Package:cn.minglg.interview.ai.render
 * Description:自定义模板渲染器，因为默认的StTemplateRenderer占位符只支持单个字符，不够灵活
 *
 * @Author kfzx-minglg
 * @Create 2025/8/10
 * @Version 1.0
 */
@Data
public class CustomMultiCharTemplateRenderer implements TemplateRenderer {
    private final String startDelimiter;
    private final String endDelimiter;
    private final Pattern pattern;

    /**
     * 私有化构造器，只能通过建造者模式构建对象，
     * Builder注解标注在构造器而不是类上，确保在builder中使用自定义的构造器实例化
     */
    @Builder
    private CustomMultiCharTemplateRenderer(String startDelimiter, String endDelimiter) {
        this.startDelimiter = startDelimiter == null ? "<" : startDelimiter;
        this.endDelimiter = endDelimiter == null ? ">" : endDelimiter;
        this.pattern = Pattern.compile(
                Pattern.quote(this.startDelimiter) + "([a-zA-Z_][a-zA-Z0-9_]*)" + Pattern.quote(this.endDelimiter)
        );
    }

    @Override
    public String apply(String template, Map<String, Object> variables) {
        if (!StringUtils.hasText(template) || variables == null) {
            return template;
        }

        StringBuilder result = new StringBuilder();
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);

            // 如果变量不存在，保留原占位符
            String replacement = value != null ? value.toString() :
                    startDelimiter + variableName + endDelimiter;
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}