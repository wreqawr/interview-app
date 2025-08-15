package cn.minglg.interview.resume.service.impl;


import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.minglg.interview.ai.core.resume.AiResumeCoreService;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.annotation.AsyncTaskQuery;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.constant.resume.ResumeStatus;
import cn.minglg.interview.common.constant.task.TaskStatus;
import cn.minglg.interview.common.exception.NoSuchResumeException;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.FileUtils;
import cn.minglg.interview.common.utils.TaskUtils;
import cn.minglg.interview.common.utils.UserUtils;
import cn.minglg.interview.minio.service.MinioService;
import cn.minglg.interview.resume.exception.ResumeDeleteException;
import cn.minglg.interview.resume.exception.ResumeDownloadException;
import cn.minglg.interview.resume.exception.ResumePreviewException;
import cn.minglg.interview.resume.exception.ResumeUploadException;
import cn.minglg.interview.resume.mapper.ResumeMetadataMapper;
import cn.minglg.interview.resume.pojo.ResumeDetail;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import cn.minglg.interview.resume.repository.ResumeDetailRepository;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.parser.AutoDetectParser;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
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
    private final AutoDetectParser autoDetectParser;
    private final ResumeMetadataMapper resumeMetadataMapper;
    private final ResumeDetailRepository resumeDetailRepository;
    private final AiResumeCoreService aiResumeCoreService;
    private final StringRedisTemplate redisTemplate;


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

        // 第一步：基础校验
        if (file.isEmpty()) {
            throw new ResumeUploadException("上传文件大小不能为空！");
        }

        // 第二步：文件名安全处理
        String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream is1 = file.getInputStream();
             InputStream is2 = file.getInputStream()) {
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
            String sha256 = new Digester(DigestAlgorithm.SHA256).digestHex(is1);
            Long fileSize = file.getSize();
            String mimeType = Objects.requireNonNull(file.getContentType());
            boolean previewEnabled = isPreviewEnabled(originalName, globalProperties.getResume().getAllowPreviewTypes());
            minioService.uploadFile(bucketName, file, objectName);

            // 第五步：封装简历元信息
            ResumeMetadata resumeMetadata = ResumeMetadata.builder()
                    .resumeId(resumeId)
                    .viewCount(0)
                    .downloadCount(0)
                    .rate(new BigDecimal("4.8"))
                    .userId(userId)
                    .bucketName(bucketName)
                    .objectName(objectName)
                    .originalName(originalName)
                    .fileSize(fileSize)
                    .mimeType(mimeType)
                    .previewEnabled(previewEnabled)
                    .sha256(sha256)
                    .status(ResumeStatus.EFFECTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            // 第六步：使用Tika提取简历文本信息
            String content = FileUtils.getContentFromFile(autoDetectParser, is2);
            // 第七步：调用异步方法，分析提取简历信息，并持久化保存
            String taskId = TaskUtils.generateTaskId();
            aiResumeCoreService.resumeSummarizeAndSave(userId, taskId, resumeId, content, resumeMetadata);
            // 第八步：构建响应
            Map<String, ? extends Serializable> data = Map.of("taskId", taskId, "resumeId", resumeId);

            return R.builder().code(ResponseCode.OK.getCode())
                    .message("简历上传成功，请等待后台解析！")
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
     * @param resumeIds 文件编号列表
     * @return 文件流
     */

    @Override
    public R resumeDownload(String[] resumeIds) {
        User currentUser = UserUtils.getCurrentUser();
        List<String> resumeIdList = Arrays.stream(resumeIds).toList();
        List<ResumeMetadata> resumeMetadataList = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeIdList(currentUser.getUserId(), resumeIdList);
        if (resumeMetadataList == null) {
            throw new ResumeDownloadException("简历信息不存在！");
        }
        List<Map<String, String>> data = new ArrayList<>();
        for (ResumeMetadata resumeMetadata : resumeMetadataList) {
            String bucketName = resumeMetadata.getBucketName();
            String objectName = resumeMetadata.getObjectName();
            String sha256 = resumeMetadata.getSha256();
            String downloadFileName = resumeMetadata.getOriginalName();
            Integer downloadExpired = globalProperties.getResume().getDownloadExpired();
            try {
                String downloadUrl = minioService.getFileUrl(bucketName, objectName, downloadExpired);
                data.add(Map.of("downloadUrl", downloadUrl,
                        "downloadFileName", downloadFileName,
                        "sha256", sha256));
                resumeMetadata.setDownloadCount(resumeMetadata.getDownloadCount() + 1);
                resumeMetadataMapper.updateResumeMetadata(currentUser.getUserId(), TaskUtils.generateTaskId(), resumeMetadata);
            } catch (Exception e) {
                throw new ResumeDownloadException(e.getMessage());
            }
        }
        return R.builder()
                .code(ResponseCode.OK.getCode())
                .message("下载链接获取成功！")
                .data(data)
                .build();
    }

    /**
     * 简历删除接口，支持一次删除多个文件
     *
     * @param resumeIds 简历id列表
     * @return 操作结果
     */

    @Override
    public R resumeDelete(String[] resumeIds) {
        R result;
        User currentUser = UserUtils.getCurrentUser();
        if (resumeIds == null || resumeIds.length == 0) {
            throw new ResumeDeleteException("简历信息不能为空！");
        }
        try {
            Long userId = currentUser.getUserId();
            List<String> resumeIdList = Arrays.stream(resumeIds).toList();
            List<ResumeMetadata> resumeMetadataList = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeIdList(userId, resumeIdList);
            // 第一步：删除mysql中元信息
            int affectRows = resumeMetadataMapper.deleteResumeMetadataByUserIdAndResumeId(userId, resumeIdList);
            if (affectRows == 0) {
                throw new ResumeDeleteException("删除失败，简历信息不存在！");
            }
            // 第二步：删除mongodb中的详细信息
            resumeIdList.forEach(resumeId -> resumeDetailRepository.deleteResumeDetailByUserIdAndResumeId(userId, resumeId));
            // 第三步：删除minio存储的物理文件
            for (ResumeMetadata resumeMetadata : resumeMetadataList) {
                String bucketName = resumeMetadata.getBucketName();
                String objectName = resumeMetadata.getObjectName();
                minioService.deleteFile(bucketName, objectName);
            }
            // 第四步：删除redis中存储的简历信息
            List<String> redisKeyList = resumeIdList.stream().map(resumeId -> globalProperties.getResume().getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId).toList();
            redisTemplate.delete(redisKeyList);
            // 第五步：构建响应
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
        List<ResumeMetadata> resumeMetadataList = resumeMetadataMapper.getResumeMetadataByUserId(currentUser.getUserId());
        String message = resumeMetadataList == null ? "未查询到当前用户的简历信息！" : "简历信息获取成功！";
        return R.builder()
                .code(ResponseCode.OK.getCode())
                .data(resumeMetadataList)
                .message(message)
                .build();
    }

    /**
     * 简历信息提取结果查询接口
     *
     * @param userId   用户id
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 查询结果
     */
    @Override
    @AsyncTaskQuery
    public R getResumeAsyncUploadResult(Long userId, String taskId, String resumeId) {
        ResumeMetadata resumeMetadata = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeId(userId, resumeId);
        return R.builder()
                .code(ResponseCode.OK.getCode())
                .data(resumeMetadata)
                .message("简历信息提取成功")
                .build();
    }

    /**
     * 获取简历预览url
     *
     * @param resumeId 简历id
     * @return 简历预览url
     */
    @Override
    public R resumePreview(String resumeId) {
        User currentUser = UserUtils.getCurrentUser();
        ResumeMetadata resumeMetadata = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeId(currentUser.getUserId(), resumeId);
        if (resumeMetadata == null) {
            throw new ResumePreviewException("简历不存在！");
        }
        Boolean previewEnabled = resumeMetadata.getPreviewEnabled();
        if (!previewEnabled) {
            throw new ResumePreviewException("文件不支持预览！");
        }
        String bucketName = resumeMetadata.getBucketName();
        String objectName = resumeMetadata.getObjectName();
        Integer expired = globalProperties.getResume().getPreviewExpired();
        try {
            String fileUrl = minioService.getFileUrl(bucketName, objectName, expired);
            String taskId = TaskUtils.generateTaskId();
            resumeMetadata.setViewCount(resumeMetadata.getViewCount() + 1);
            resumeMetadataMapper.updateResumeMetadata(currentUser.getUserId(), taskId, resumeMetadata);
            return R.builder().code(ResponseCode.OK.getCode())
                    .data(Map.of("previewUrl", fileUrl))
                    .message("预览地址获取成功！")
                    .build();
        } catch (Exception e) {
            throw new ResumePreviewException(e.getMessage());
        }
    }

    /**
     * 简历分析（面向求职者）
     *
     * @param resumeId 简历id
     * @return 同步查询直接返回查询结果，异步查询则返回taskId
     */
    @Override
    public R resumeAnalyze(String resumeId) {
        User currentUser = UserUtils.getCurrentUser();
        Long userId = currentUser.getUserId();
        String redisKey = globalProperties.getResume().getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId;
        String hashKeyForAnalyze = "analyzeHtmlContent";
        String hashKeyForAnalyzeStatus = "analyzeStatus";
        String hashKeyForAnalyzeTaskId = "analyzeTaskId";
        // 第一步：查询该简历是否已经处于分析状态，如是则避免重复请求，消耗资源
        String status = (String) redisTemplate.opsForHash().get(redisKey, hashKeyForAnalyzeStatus);
        String runningTaskId = (String) redisTemplate.opsForHash().get(redisKey, hashKeyForAnalyzeTaskId);
        if (TaskStatus.RUNNING.toString().equals(status) && runningTaskId != null) {
            return R.builder()
                    .code(ResponseCode.ASYNC_TASK_RUNNING.getCode())
                    .data(Map.of("taskId", runningTaskId))
                    .message("简历正在分析中，请勿重复提交！")
                    .build();
        }
        // 第二步：首先查询数据库中是否已经保存了该简历的解析结果，采用：Redis-mongodb-ai，3级缓存提高查询效率
        // 一级缓存：redis读取

        String analyzeResult = (String) redisTemplate.opsForHash().get(redisKey, hashKeyForAnalyze);
        if (StringUtils.hasText(analyzeResult)) {
            return R.builder()
                    .code(ResponseCode.OK.getCode())
                    .data(analyzeResult)
                    .message("简历分析完毕！")
                    .build();
        }
        // 二级缓存从mongodb取，能取到就回写redis
        ResumeDetail resumeDetail = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId);
        if (resumeDetail == null) {
            throw new NoSuchResumeException("简历信息不存在！");
        }
        analyzeResult = resumeDetail.getResumeAnalyzeHtmlContentForJobSeekers();
        if (StringUtils.hasText(analyzeResult)) {
            redisTemplate.opsForHash().put(redisKey, hashKeyForAnalyze, analyzeResult);
            return R.builder()
                    .code(ResponseCode.OK.getCode())
                    .data(analyzeResult)
                    .message("简历分析结果获取成功！")
                    .build();
        }
        // 三级缓存从ai获取，触发异步任务
        String taskId = TaskUtils.generateTaskId();
        aiResumeCoreService.resumeAnalyzeAndSave(userId, taskId, resumeId);
        return R.builder()
                .code(ResponseCode.ASYNC_TASK_RUNNING.getCode())
                .data(Map.of("taskId", taskId))
                .message("正在后台分析中，请稍后！")
                .build();
    }

    /**
     * 获取简历分析异步结果
     *
     * @param userId   用户id
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 结果信息
     */
    @Override
    @AsyncTaskQuery
    public R getResumeAsyncAnalyzeResult(Long userId, String taskId, String resumeId) {
        String redisKey = globalProperties.getResume().getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId;
        String hashKey = "analyzeHtmlContent";
        String analyzeResult = ((String) redisTemplate.opsForHash().get(redisKey, hashKey));
        if (StringUtils.hasText(analyzeResult)) {
            return R.builder()
                    .code(ResponseCode.OK.getCode())
                    .data(analyzeResult)
                    .message("简历分析完毕！")
                    .build();
        }
        String result = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId).getResumeAnalyzeHtmlContentForJobSeekers();
        return R.builder()
                .code(ResponseCode.OK.getCode())
                .data(result)
                .message("简历分析完毕！")
                .build();
    }
}
