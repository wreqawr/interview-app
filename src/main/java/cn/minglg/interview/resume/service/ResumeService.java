package cn.minglg.interview.resume.service;

import cn.minglg.interview.common.response.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
    R resumeUpload(MultipartFile file);

    /**
     * 简历下载接口
     *
     * @param fileName 文件名
     * @return 文件流
     */
    Map<String, Object> resumeDownload(String fileName);

    /**
     * 简历删除接口
     *
     * @param resumeIds 简历id列表
     * @return 操作结果
     */
    R resumeDelete(String[] resumeIds);

    /**
     * 简历元信息展示接口，获取当前用户的所有简历元信息
     *
     * @return 简历元信息列表
     */
    R resumeMetadataDisplay();

    /**
     * 简历信息提取结果查询接口
     *
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 查询结果
     */
    R getResumeAsyncUploadResult(String taskId, String resumeId);

    /**
     * 获取简历预览url
     *
     * @param resumeId 简历id
     * @return 简历预览url
     */

    R getResumePreviewUrl(String resumeId);

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
