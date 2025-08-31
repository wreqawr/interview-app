package cn.minglg.interview.interview.converter;

import cn.minglg.interview.interview.pojo.Job;
import io.r2dbc.spi.Row;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ClassName:JobConverter
 * Package:cn.minglg.interview.interview.converter
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/31
 * @Version 1.0
 */
@ReadingConverter
public class JobConverter implements Converter<Row, Job> {

    /**
     * 将数据库查询结果行（Row）转换为 Job 实体对象。
     * 该方法从给定的 Row 对象中提取各个字段的值，并将其映射到 Job 对象的对应属性中。
     * 包括基础信息、薪资范围、工作描述、状态等字段，同时也处理了枚举类型的转换。
     *
     * @param source 数据库查询返回的一行记录，必须非空
     * @return 构建完成的 Job 实体对象
     */
    @Override
    public Job convert(@NotNull Row source) {
        return Job.builder()
                // 基本信息字段映射
                .jobId(source.get("job_id", Long.class))
                .companyId(source.get("company_id", Long.class))
                .companyName(source.get("company_name", String.class))
                .hrUserId(source.get("hr_user_id", Long.class))
                .jobTitle(source.get("job_title", String.class))
                // 枚举类型字段转换
                .jobType(Job.JobType.valueOf(source.get("job_type", String.class)))
                .jobLevel(Job.JobLevel.valueOf(source.get("job_level", String.class)))
                // 部门与工作地点
                .department(source.get("department", String.class))
                .workLocation(source.get("work_location", String.class))
                // 薪资相关信息
                .salaryMin(source.get("salary_min", BigDecimal.class))
                .salaryMax(source.get("salary_max", BigDecimal.class))
                .salaryRange(source.get("salary_range", String.class))
                // 经验与教育要求
                .experienceYears(source.get("experience_years", Integer.class))
                .educationLevel(Job.EducationLevel.valueOf(source.get("education_level", String.class)))
                // 工作详情描述相关字段
                .jobDescription(source.get("job_description", String.class))
                .jobRequirements(source.get("job_requirements", String.class))
                .jobResponsibilities(source.get("job_responsibilities", String.class))
                .benefits(source.get("benefits", String.class))
                // 联系方式与截止时间
                .contactInfo(source.get("contact_info", String.class))
                .applicationDeadline(source.get("application_deadline", LocalDate.class))
                // 状态与统计数据
                .status(Job.JobStatus.valueOf(source.get("status", String.class)))
                .viewCount(source.get("view_count", Integer.class))
                .applicationCount(source.get("application_count", Integer.class))
                // 特殊标记字段
                .isFeatured(source.get("is_featured", Boolean.class))
                .isUrgent(source.get("is_urgent", Boolean.class))
                // 标签与时间戳字段
                .tags(source.get("tags", String.class))
                .createdTime(source.get("created_time", LocalDateTime.class))
                .updatedTime(source.get("updated_time", LocalDateTime.class))
                .publishedTime(source.get("published_time", LocalDateTime.class))
                .closedTime(source.get("closed_time", LocalDateTime.class))
                .build();
    }

}
