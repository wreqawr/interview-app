package cn.minglg.candidate.dto;

import lombok.Data;

/**
 * ClassName:JobDTO
 * Package:cn.minglg.interview.job.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/30
 * @Version 1.0
 */
@Data
public class JobDTO {
    /**
     * 职位ID
     */
    private Long jobId;
    /**
     * 职位标题
     */
    private String jobTitle;
    /**
     * 公司名称
     */
    private String companyName;
}
