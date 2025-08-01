package cn.minglg.interview.resume.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
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
public class ResumeMetadata implements Serializable {
    /**
     * 文件ID
     */
    private String resumeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * MinIO桶名称
     */
    private String bucketName;

    /**
     * MinIO对象名称
     */
    private String objectName;

    /**
     * 文件下载路径
     */
    private String objectUrl;

    /**
     * 原始文件名
     */
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
     * 文件哈希值（校验用）
     */
    private String sha256;

    /**
     * 文件状态
     */
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
