package cn.minglg.interview.resume.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * ClassName:ResumeDetail
 * Package:cn.minglg.interview.resume.pojo
 * Description:简历详细信息实体类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/2
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "c_resume_detail")
public class ResumeDetail {
    /**
     * 简历id
     */
    @Id
    private String resumeId;
    /**
     * 用户id
     */
    @JsonIgnore
    private Long userId;
    /**
     * 任务id
     */
    @JsonIgnore
    private String taskId;
    /**
     * 基本信息
     */
    private BasicInfo basicInfo;
    /**
     * 工作经历
     */
    private List<WorkExperience> workExperience;
    /**
     * 教育背景
     */
    private List<Education> education;
    /**
     * 技能
     */
    private Skills skills;
    /**
     * 项目经历
     */
    private List<Project> projects;

    @Data
    private static class BasicInfo {
        private String name;         // 姓名
        private String phone;        // 手机号
        private String email;        // 邮箱
        private String location;     // 当前所在地（城市）
        private Float yearsExp;     // 工作年限（年）
        private String targetTitle; // 求职目标职位
    }

    @Data
    private static class WorkExperience {
        private String company;       // 公司名称
        private String title;         // 职位名称
        private String duration;      // 时间段（YYYY.MM-YYYY.MM）
        private String description;   // 职责描述
        private List<String> achievements; // 主要业绩
        private List<String> techStack;  // 技术栈
    }

    @Data
    private static class Education {
        private String school;    // 学校名称
        private String degree;    // 学历（本科/硕士等）
        private String major;     // 专业
        private String duration;  // 时间段（YYYY.MM-YYYY.MM）
        private Float gpa;        // GPA
    }

    @Data
    private static class Skills {
        private List<String> technical;   // 技术技能
        private List<Language> languages; // 语言能力
        private List<String> certificates; // 证书

        @Data
        private static class Language {
            private String language; // 语言名称
            private String level;    // 熟练程度
        }
    }

    @Data
    private static class Project {
        private String name;       // 项目名称
        private String role;       // 担任角色
        private String duration;   // 时间段
        private String description; // 项目描述
        private List<String> achievements; // 项目成果
        private List<String> techStack;  // 技术栈
    }
}
