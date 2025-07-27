package cn.minglg.interview.resume.config;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:ResumeParserConfig
 * Package:cn.minglg.interview.resume.config
 * Description:简历解析器配置类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/26
 * @Version 1.0
 */
@Configuration
public class ResumeParserConfig {


    @Bean("parserMap")
    public Map<String, Parser> getParserMap() {
        Map<String, Parser> parserMap = new HashMap<>();
        parserMap.put("auto", new AutoDetectParser());
        parserMap.put(".pdf", new PDFParser());
        parserMap.put(".doc", new OOXMLParser());
        parserMap.put(".docx", parserMap.get(".doc"));
        return parserMap;
    }

}
