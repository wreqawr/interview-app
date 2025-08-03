package cn.minglg.interview.resume.service.impl;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.constant.ResumeStatus;
import cn.minglg.interview.common.constant.TaskStatus;
import cn.minglg.interview.common.exception.NoSuchTaskException;
import cn.minglg.interview.common.exception.UnKnowUserException;
import cn.minglg.interview.common.mapper.TaskMapper;
import cn.minglg.interview.common.pojo.Task;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.TaskUtils;
import cn.minglg.interview.common.utils.UserUtils;
import cn.minglg.interview.minio.service.MinioService;
import cn.minglg.interview.resume.exception.ResumeDeleteException;
import cn.minglg.interview.resume.exception.ResumeDownloadException;
import cn.minglg.interview.resume.exception.ResumeUploadException;
import cn.minglg.interview.resume.mapper.ResumeMetadataMapper;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * ClassName:ResumeServiceImpl
 * Package:cn.minglg.interview.resume.service.impl
 * Description:简历服务实现类
 *
 * @Author kfzx-minglg
 * @Create 2025/7/23
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class ResumeServiceImpl implements ResumeService {
    private final GlobalProperties globalProperties;
    private final MinioService minioService;
    private final ResumeMetadataMapper resumeMetadataMapper;
    private final TaskMapper taskMapper;
    private final ResumeDetailRepository resumeDetailRepository;


    /**
     * 简历上传接口
     *
     * @param file 文件对象
     * @return 上传结果
     */
    @Override
    public R resumeUpload(MultipartFile file) {
        List<String> allowFileTypes = globalProperties.getResume().getAllowFileTypes();
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效请求用户！");
        }

        // 第一步：基础校验
        if (file.isEmpty()) {
            throw new ResumeUploadException("上传文件大小不能为空！");
        }

        // 第二步：文件名安全处理
        String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream is = file.getInputStream()) {
            // 第三步：校验文件格式（根据需要扩展）
            if (!isValidFileType(originalName, allowFileTypes)) {
                throw new ResumeUploadException("不支持的文件格式！");
            }

            // 第三步：文件大小校验（自动生效于application配置）

            // 第四步：文件保存至Minio
            String resumeId = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 15);
            Long userId = currentUser.getUserId();
            String bucketName = globalProperties.getMinio().getBucketNamePrefix().get("resumeUpload") + userId;
            String objectName = System.currentTimeMillis() + getFileExtension(originalName);
            String sha256 = new Digester(DigestAlgorithm.SHA256).digestHex(is);
            Long fileSize = file.getSize();
            String mimeType = Objects.requireNonNull(file.getContentType());
            minioService.uploadFile(bucketName, file, objectName);
            String objectUrl = minioService.getFileUrl(bucketName, objectName);

            // 第五步：保存简历元信息至mysql
            ResumeMetadata resumeMetadata = ResumeMetadata.builder()
                    .resumeId(resumeId)
                    .userId(userId)
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .objectUrl(objectUrl)
                    .originalName(originalName)
                    .fileSize(fileSize)
                    .mimeType(mimeType)
                    .sha256(sha256)
                    .status(ResumeStatus.EFFECTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            resumeMetadataMapper.addResumeMetadata(resumeMetadata);

            // 第六步：构建响应
            Map<String, ? extends Serializable> data = Map.of("originalName", originalName,
                    "objectName", objectName,
                    "mimeType", mimeType,
                    "fileSize", fileSize,
                    "sha256", sha256);

            return R.builder().code(ResponseCode.OK.getCode())
                    .message("文件上传成功！")
                    .data(data)
                    .build();
        } catch (Exception e) {
            return R.builder().code(ResponseCode.RESUME_UPLOAD_FAIL.getCode())
                    .message("简历上传失败，原因为：" + e.getMessage())
                    .build();
        }
    }

    /**
     * 简历下载接口
     *
     * @param resumeId 文件编号
     * @return 文件流
     */

    @Override
    public Map<String, Object> resumeDownload(String resumeId) {
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效请求用户！");
        }
        try {
            Long userId = currentUser.getUserId();
            ResumeMetadata metadata = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeId(userId, resumeId);
            if (metadata == null) {
                throw new ResumeDeleteException("简历不存在！");
            }
            String bucketName = metadata.getBucketName();
            String objectName = metadata.getObjectName();

            InputStreamResource isr = new InputStreamResource(minioService.downloadFile(bucketName, objectName));
            String contentType = minioService.getContentType(bucketName, objectName);
            return Map.of("isr", isr, "contentType", contentType);

        } catch (Exception e) {
            throw new ResumeDownloadException(e.getMessage());
        }
    }

    /**
     * 简历删除接口
     *
     * @param resumeIds 简历id列表
     * @return 操作结果
     */

    @Override
    public R resumeDelete(String[] resumeIds) {
        R result;
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效用户！");
        }
        if (resumeIds == null || resumeIds.length == 0) {
            throw new ResumeDeleteException("简历信息不能为空！");
        }
        try {
            Long userId = currentUser.getUserId();
            List<String> resumeIdList = Arrays.stream(resumeIds).toList();
            List<ResumeMetadata> resumeMetadataList = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeIdList(userId, resumeIdList);
            int affectRows = resumeMetadataMapper.deleteResumeMetadataByUserIdAndResumeId(userId, resumeIdList);
            if (affectRows == 0) {
                throw new ResumeDeleteException("删除失败，简历信息不存在！");
            }
            for (ResumeMetadata resumeMetadata : resumeMetadataList) {
                String bucketName = resumeMetadata.getBucketName();
                String objectName = resumeMetadata.getObjectName();
                minioService.deleteFile(bucketName, objectName);
            }
            result = R.builder()
                    .code(ResponseCode.OK.getCode())
                    .message("简历删除成功！").build();
        } catch (Exception e) {
            result = R.builder()
                    .code(ResponseCode.RESUME_DELETE_FAIL.getCode())
                    .message("简历删除失败，原因为：" + e.getMessage()).build();

        }
        return result;
    }

    /**
     * 简历展示接口，获取当前用户的所有简历元信息
     *
     * @return 简历信息列表
     */
    @Override
    public R resumeMetadataDisplay() {
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效用户！");
        }
        List<String> resumeIdList = resumeMetadataMapper.getResumeMetadataByUserId(currentUser.getUserId())
                .stream()
                .map(ResumeMetadata::getResumeId)
                .toList();
        return R.builder()
                .code(ResponseCode.OK.getCode())
                .data(resumeIdList)
                .message("简历信息获取成功！")
                .build();
    }

    /**
     * 简历信息提取结果查询接口
     *
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 查询结果
     */
    @Override
    public R getResumeSummarizeResult(String taskId, String resumeId) {
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效用户！");
        }
        Task task = TaskUtils.queryTaskByUserIdAndTaskId(taskMapper, currentUser.getUserId(), taskId);
        if (task == null) {
            throw new NoSuchTaskException("任务查询失败，不存在taskId为" + taskId + "的任务！");
        }
        TaskStatus taskStatus = task.getTaskStatus();
        if (taskStatus == TaskStatus.FINISHED) {
            ResumeDetail queryResult = resumeDetailRepository.findByUserIdAndResumeId(currentUser.getUserId(), resumeId);
            return R.builder()
                    .code(ResponseCode.OK.getCode())
                    .data(queryResult)
                    .message(task.getTaskStatus().getDescription())
                    .build();
        } else if (taskStatus == TaskStatus.FAILED) {
            return R.builder()
                    .code(ResponseCode.ASYNC_TASK_FAIL.getCode())
                    .message(task.getErrorMessage())
                    .build();
        } else {
            return R.builder()
                    .code(ResponseCode.ASYNC_TASK_RUNNING.getCode())
                    .message(task.getTaskStatus().getDescription())
                    .build();
        }
    }
}
