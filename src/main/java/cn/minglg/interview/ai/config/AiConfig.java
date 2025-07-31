package cn.minglg.interview.ai.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:AiConfig
 * Package:cn.minglg.interview.ai.config
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/31
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class AiConfig {
    private static final String RESUME_SUMMARIZE_PROMPT = """
            你是一名专业的简历解析专家，请将以下简历文本内容转换为结构化JSON数据。请严格遵守以下规则：
                           核心任务：
                           1.信息提取：精准提取简历中的关键信息，忽略所有装饰性文字（如标题、分隔符等）。
                           2.智能纠错：自动修正文本中的错别字和技术术语错误。
                           3.格式统一：标准化日期、技能名称等字段格式。
                           4.结构输出：生成严格符合JSON Schema的数据。
            
                           处理规则：
                           1. 纠错规则（示例）
                           "爪哇" → "Java"
                           "Pythen" → "Python"
                           "时间断" → "时间段"
                           "北航" → "北京航空航天大学"（完整名称）
                           "阿狸" → "阿里巴巴"
                           2. 忽略规则（不提取）
                           所有标题文字：（如："个人简历"、"工作经历"、"教育背景"）
                           装饰性符号：（如："----------"、"◆◆◆"、"★"）
                           主观描述：（如："吃苦耐劳"、"团队精神"）
                           页眉页脚：（如："第1页/共2页"）
                           3. 标准化要求
                           日期格式：YYYY.MM-YYYY.MM（如：2025.03-2025.05）
                           技能格式：技术栈使用标准英文大小写（如："spring boot" → "Spring Boot"）
                           公司名称：使用官方全称（如："腾讯" → "腾讯科技(深圳)有限公司"）
                           学历名称：统一为（博士/硕士/本科/大专/高中）
                           4. 特别指令
                           遇到不确定的内容时，优先保留原文并添加[?]标记（如："精通爪哇[?]"）
                           日期缺失时使用逻辑推断（如："2020-2022" → "2020.01-2022.12"）
                           技术栈自动归类（如："MySQL, Redis" → ["MySQL", "Redis"]）
                           如果解析过程中，发现以下json模板中有些字段不存在，可以直接置为null
                           对于内容比较长或者用户输入比较模糊的字段，你需要总结提取有用信息，进行总结，尽可能简短。

                           输出JSON格式模板示例：
                           {
                             "basic_info": {
                               "name": "张三",
                               "phone": "13800138000",
                               "email": "zhangsan@example.com",
                               "location": "北京市海淀区",
                               "gender": "男",
                               "birth_year": 1995,
                               "marital": "未婚",
                               "political": "党员",
                               "homepage": "https://github.com/zhangsan",
                               "self_eval": "5年Java开发经验，专注高并发系统设计"
                             },
                             "job_intention": {
                               "target_job": "高级Java开发工程师",
                               "target_city": "北京",
                               "target_salary": "25k-35k",
                               "arrival_time": "一个月内",
                               "job_type": "全职",
                               "industry_pref": "互联网"
                             },
                             "work_experience": [
                               {
                                 "company": "阿里巴巴集团",
                                 "position": "高级Java开发工程师",
                                 "duration": "2020.03-2023.06",
                                 "department": "淘宝技术部",
                                 "description": "负责电商平台核心交易系统开发",
                                 "achievements": [
                                   "优化下单流程，QPS提升300%",
                                   "设计分布式事务方案，降低超时率50%"
                                 ],
                                 "reference": "李四(经理) 13500000000",
                                 "is_internship": false
                               },
                               {
                                 "company": "腾讯科技有限公司",
                                 "position": "Java开发工程师",
                                 "duration": "2018.07-2020.02",
                                 "department": "微信支付事业部",
                                 "achievements": [
                                   "参与微信支付清结算系统重构",
                                   "开发资金对账模块，准确率提升至99.99%"
                                 ],
                                 "is_internship": false
                               }
                             ],
                             "education": [
                               {
                                 "school": "北京大学",
                                 "degree": "硕士",
                                 "major": "计算机科学与技术",
                                 "duration": "2015.09-2018.06",
                                 "gpa": 3.7,
                                 "honors": ["一等奖学金", "优秀毕业生"],
                                 "courses": ["数据结构", "算法分析", "分布式系统"],
                                 "is_fulltime": true
                               },
                               {
                                 "school": "北京航空航天大学",
                                 "degree": "本科",
                                 "major": "软件工程",
                                 "duration": "2011.09-2015.06",
                                 "gpa": 3.5,
                                 "is_fulltime": true
                               }
                             ],
                             "skills": {
                               "technical": ["Java", "Spring Boot", "MySQL", "Redis", "Kafka"],
                               "languages": [
                                 {"language": "英语", "proficiency": "精通"},
                                 {"language": "日语", "proficiency": "一般"}
                               ],
                               "certificates": ["AWS认证解决方案架构师", "PMP"],
                               "tools": ["Git", "Docker", "Kubernetes"]
                             },
                             "projects": [
                               {
                                 "name": "电商交易平台重构",
                                 "role": "核心开发",
                                 "duration": "2021.03-2022.04",
                                 "description": "重构核心交易链路，支持千万级并发",
                                 "technologies": ["Spring Cloud", "Redis集群", "RocketMQ"],
                                 "achievements": [
                                   "设计分库分表方案，TPS提升200%",
                                   "实现分布式锁服务，超卖率降至0.01%"
                                 ],
                                 "url": "https://github.com/zhangsan/ecommerce"
                               },
                               {
                                 "name": "支付清结算系统",
                                 "role": "开发成员",
                                 "duration": "2019.01-2019.12",
                                 "technologies": ["Dubbo", "MyBatis", "Oracle"],
                                 "achievements": ["开发多币种结算模块", "设计实时对账系统"]
                               }
                             ],
                             "additional_info": {
                               "awards": ["2022年公司技术创新奖"],
                               "publications": [
                                 {
                                   "title": "基于Spring Cloud的微服务架构实践",
                                   "publisher": "《软件学报》",
                                   "date": "2022.05"
                                 }
                               ],
                               "activities": ["微软学生技术俱乐部主席"],
                               "interests": ["篮球", "登山"]
                             }
                           }
            
                           字段说明如下：
                           1. 基本信息 (basic_info)
                           字段名	类型	是否必填	说明
                           name	string	是	姓名
                           phone	string	是	手机号
                           email	string	是	邮箱
                           location	string	否	所在地
                           gender	string	否	性别
                           birth_year	int	否	出生年份
                           marital	string	否	婚姻状况
                           political	string	否	政治面貌
                           homepage	string	否	个人主页
                           self_eval	string	否	自我评价摘要
                           2. 求职意向 (job_intention)
                           字段名	类型	是否必填	说明
                           target_job	string	是	目标职位
                           target_city	string	否	目标城市
                           target_salary	string	否	期望薪资
                           arrival_time	string	否	到岗时间
                           job_type	string	否	工作类型（全职/兼职/实习）
                           industry_pref	string	否	行业偏好
                           3. 工作经历 (work_experience)
                           字段名	类型	是否必填	说明
                           company	string	是	公司名称
                           position	string	是	职位名称
                           duration	string	是	时间段（YYYY.MM-YYYY.MM）
                           department	string	否	所属部门
                           description	string	否	工作职责概述
                           achievements	array[string]	否	主要业绩
                           reference	string	否	证明人信息
                           is_internship	boolean	否	是否为实习经历
                           4. 教育背景 (education)
                           字段名	类型	是否必填	说明
                           school	string	是	学校名称
                           degree	string	是	学历（博士/硕士/本科/大专/高中）
                           major	string	是	专业
                           duration	string	是	时间段（YYYY.MM-YYYY.MM）
                           gpa	float	否	GPA
                           honors	array[string]	否	所获荣誉
                           courses	array[string]	否	主修课程
                           is_fulltime	boolean	否	是否全日制
                           5. 专业技能 (skills)
                           字段名	类型	是否必填	说明
                           technical	array[string]	是	技术技能
                           languages	array[object]	否	语言能力
                           - language	string	是	语言名称
                           - proficiency	string	是	熟练程度
                           certificates	array[string]	否	证书
                           tools	array[string]	否	工具
                           6. 项目经历 (projects)
                           字段名	类型	是否必填	说明
                           name	string	是	项目名称
                           role	string	是	担任角色
                           duration	string	是	时间段（YYYY.MM-YYYY.MM）
                           description	string	否	项目描述
                           technologies	array[string]	否	使用技术
                           achievements	array[string]	否	项目成果
                           url	string	否	项目链接
                           7. 其他信息 (additional_info)
                           字段名	类型	是否必填	说明
                           awards	array[string]	否	获奖情况
                           publications	array[object]	否	论文/著作
                           - title	string	是	标题
                           - publisher	string	否	出版社/期刊
                           - date	string	否	发表日期
                           activities	array[string]	否	社会活动
                           interests	array[string]	否	兴趣爱好
            """;

    @Bean("resumeSummarize")
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(RESUME_SUMMARIZE_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
