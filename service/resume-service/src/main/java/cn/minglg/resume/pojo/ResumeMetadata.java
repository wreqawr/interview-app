package cn.minglg.resume.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ClassName:ResumeMetadata
 * Package:cn.minglg.interview.resume.pojo
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/1
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_resume_metadata")
public class ResumeMetadata implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "resume_id", type = IdType.NONE)
    private String resumeId;

    /**
     * 简历标题
     */
    private String resumeTitle;

    /**
     * 查看次数
     */
    private Integer viewCount;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 综合评分
     */
    private BigDecimal rate;

    /**
     * 用户ID
     */
    @JsonIgnore
    private Long userId;

    /**
     * MinIO桶名称
     */
    @JsonIgnore
    private String bucketName;

    /**
     * MinIO对象名称
     */
    @JsonIgnore
    private String objectName;

    /**
     * 原始文件名
     */
    @JsonIgnore
    private String originalName;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String mimeType;

    /**
     * 是否支持预览
     */
    private Boolean previewEnabled;

    /**
     * 文件哈希值（校验用）
     */
    private String sha256;

    /**
     * 文件状态
     */
    @JsonIgnore
    private Object status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @Serial
    private static final long serialVersionUID = 1L;
}
