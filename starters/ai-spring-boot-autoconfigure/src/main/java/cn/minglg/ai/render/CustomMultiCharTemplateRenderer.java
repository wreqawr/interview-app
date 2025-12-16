package cn.minglg.ai.render;

import lombok.Builder;
import lombok.Data;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName:CustomMultiCharTemplateRenderer
 * Package:cn.minglg.ai.render
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

    /**
     * 应用模板变量替换，将模板中的占位符替换为对应的变量值
     *
     * @param template  模板字符串，包含占位符
     * @param variables 变量映射表，键为变量名，值为变量值
     * @return 替换后的字符串，如果模板为空或变量映射表为null则返回原模板
     */
    @Override
    public String apply(String template, Map<String, Object> variables) {
        if (!StringUtils.hasText(template) || variables == null) {
            return template;
        }

        StringBuilder result = new StringBuilder();
        Matcher matcher = pattern.matcher(template);

        // 遍历所有匹配的占位符并进行替换
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