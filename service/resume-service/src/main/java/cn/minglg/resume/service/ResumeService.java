package cn.minglg.resume.service;

import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.resume.pojo.ResumeMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * ClassName:ResumeService
 * Package:cn.minglg.interview.resume.service
 * Description:简历服务
 *
 * @Author kfzx-minglg
 * @Create 2025/7/23
 * @Version 1.0
 */
public interface ResumeService {

    /**
     * 简历上传接口
     *
     * @param file 文件对象
     * @return 上传结果
     */
    GenericResponse<?> resumeUpload(MultipartFile file);

    /**
     * 简历下载接口
     *
     * @param resumeIds 文件id列表
     * @return 统一响应结构
     */
    GenericResponse<?> resumeDownload(String[] resumeIds);

    /**
     * 简历删除接口
     *
     * @param resumeIds 简历id列表
     * @return 操作结果
     */
    GenericResponse<?> resumeDelete(String[] resumeIds);

    /**
     * 简历元信息展示接口，获取当前用户的所有简历元信息
     *
     * @return 简历元信息列表
     */
    GenericResponse<?> resumeMetadataDisplay();

    /**
     * 简历信息提取结果查询接口
     *
     * @param userId   用户id
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 查询结果
     */
    GenericResponse<ResumeMetadata> getResumeAsyncUploadResult(Long userId, String taskId, String resumeId);

    /**
     * 获取简历预览url
     *
     * @param resumeId 简历id
     * @return 简历预览url
     */

    GenericResponse<?> resumePreview(String resumeId);

    /**
     * 简历分析（面向求职者）
     *
     * @param resumeId 简历id
     * @return 同步查询直接返回查询结果，异步查询则返回taskId
     */
    GenericResponse<?> resumeAnalyze(String resumeId);

    /**
     * 获取简历分析异步结果
     *
     * @param userId   用户id
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 结果信息
     */
    GenericResponse<String> getResumeAsyncAnalyzeResult(Long userId, String taskId, String resumeId);


    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 文件扩展名
     */
    default String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }

    /**
     * 文件类型白名单校验
     *
     * @param filename       文件名
     * @param allowFileTypes 是否在允许上传的白名单中
     * @return 验证结果
     */
    default boolean isValidFileType(String filename, List<String> allowFileTypes) {
        String extension = getFileExtension(filename).toLowerCase();
        return allowFileTypes.contains(extension);
    }

    /**
     * 文件是否支持预览
     *
     * @param filename            文件名
     * @param previewEnabledTypes 支持预览的文件后缀列表
     * @return 判断结果
     */
    default boolean isPreviewEnabled(String filename, List<String> previewEnabledTypes) {
        return previewEnabledTypes.contains(getFileExtension(filename).toLowerCase());
    }


}
