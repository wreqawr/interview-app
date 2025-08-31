package cn.minglg.interview.interview.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName:Job
 * Package:cn.minglg.interview.job.pojo
 * Description:岗位信息实体类
 *
 * @Author kfzx-minglg
 * @Create 2025/8/12
 * @Version 1.0
 */
@Data
@Builder
public class Job {

    /**
     * 岗位ID(自增主键)
     */
    private Long jobId;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 发布HR用户ID
     */
    private Long hrUserId;

    /**
     * 岗位标题
     */
    private String jobTitle;

    /**
     * 工作类型(全职/兼职/实习)
     */
    private JobType jobType;

    /**
     * 岗位级别(初级/中级/高级/专家)
     */
    private JobLevel jobLevel;

    /**
     * 所属部门
     */
    private String department;

    /**
     * 工作地点
     */
    private String workLocation;

    /**
     * 最低薪资(万元/年)
     */
    private BigDecimal salaryMin;

    /**
     * 最高薪资(万元/年)
     */
    private BigDecimal salaryMax;

    /**
     * 薪资范围
     */
    private String salaryRange;

    /**
     * 要求工作年限
     */
    private Integer experienceYears;

    /**
     * 最低学历要求
     */
    private EducationLevel educationLevel;

    /**
     * 岗位描述
     */
    private String jobDescription;

    /**
     * 岗位要求
     */
    private String jobRequirements;

    /**
     * 工作职责
     */
    private String jobResponsibilities;

    /**
     * 福利待遇
     */
    private String benefits;

    /**
     * 联系方式
     */
    private String contactInfo;

    /**
     * 申请截止日期
     */
    private LocalDate applicationDeadline;

    /**
     * 岗位状态(草稿/已发布/已关闭/已过期)
     */
    private JobStatus status;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 申请次数
     */
    private Integer applicationCount;

    /**
     * 是否推荐岗位
     */
    private Boolean isFeatured;

    /**
     * 是否紧急岗位
     */
    private Boolean isUrgent;

    /**
     * 岗位标签(逗号分隔)
     */
    private String tags;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

    /**
     * 关闭时间
     */
    private LocalDateTime closedTime;

    @Getter
    @AllArgsConstructor
    public enum EducationLevel {
        /**
         * 枚举值
         */
        HIGH_SCHOOL("高中"),
        BACHELOR("本科"),
        POSTGRADUATE("研究生"),
        PHD("博士");

        private final String description;

    }

    @AllArgsConstructor
    @Getter
    public enum JobLevel {
        /**
         * 枚举值
         */
        JUNIOR("初级"),
        MIDDLE("中级"),
        SENIOR("高级"),
        EXPERT("专家");
        private final String description;
    }

    @Getter
    @AllArgsConstructor
    public enum JobStatus {
        /**
         * 枚举值
         */
        DRAFT("草稿"),
        PUBLISHED("已发布"),
        CLOSED("已关闭"),
        EXPIRED("已过期");

        private final String description;

    }

    @Getter
    @AllArgsConstructor
    public enum JobType {
        /**
         * 枚举值
         */
        FULL_TIME("全职"),
        PART_TIME("兼职"),
        INTERNSHIP("实习");

        private final String description;
    }
}
