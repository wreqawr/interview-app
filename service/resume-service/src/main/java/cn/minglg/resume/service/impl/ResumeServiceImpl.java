package cn.minglg.resume.service.impl;


import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.minglg.commons.annotation.AsyncTaskQuery;
import cn.minglg.commons.model.context.RequestScopedUserContext;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.model.resume.ResumeDetail;
import cn.minglg.commons.model.resume.ResumeStatus;
import cn.minglg.commons.model.task.TaskStatus;
import cn.minglg.commons.model.user.User;
import cn.minglg.commons.utils.JsonUtils;
import cn.minglg.commons.utils.TaskUtils;
import cn.minglg.resume.bloom.ResumeMetadataBloomFilter;
import cn.minglg.resume.constants.ResumeConstants;
import cn.minglg.resume.exception.*;
import cn.minglg.resume.mapper.ResumeMetadataMapper;
import cn.minglg.resume.pojo.ResumeMetadata;
import cn.minglg.resume.properties.MinioProperties;
import cn.minglg.resume.properties.ResumeProperties;
import cn.minglg.resume.repository.ResumeDetailRepository;
import cn.minglg.resume.service.AsyncService;
import cn.minglg.resume.service.MinioService;
import cn.minglg.resume.service.ResumeService;
import cn.minglg.resume.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
@Service
public class ResumeServiceImpl implements ResumeService {
    private final ResumeProperties resumeProperties;
    private final MinioProperties minioProperties;
    private final MinioService minioService;
    private final AutoDetectParser autoDetectParser;
    private final ResumeMetadataMapper resumeMetadataMapper;
    private final ResumeDetailRepository resumeDetailRepository;
    private final StringRedisTemplate redisTemplate;
    private final RequestScopedUserContext userContext;
    private final AsyncService asyncService;
    private final ResumeMetadataBloomFilter resumeMetadataBloomFilter;

    private final String resumeRedisKeyPrefix = ResumeConstants.RESUME_METADATA_REDIS_KEY_PREFIX;


    /**
     * 简历上传接口
     *
     * @param file 文件对象
     * @return 上传结果
     */
    @Override
    public GenericResponse<?> resumeUpload(MultipartFile file) {
        List<String> allowFileTypes = resumeProperties.getAllowFileTypes();
        User currentUser = userContext.getUser();

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
            String bucketName = minioProperties.getBucketNamePrefix().get("resumeUpload") + userId;
            String objectName = System.currentTimeMillis() + getFileExtension(originalName);
            String sha256 = new Digester(DigestAlgorithm.SHA256).digestHex(is1);
            Long fileSize = file.getSize();
            String mimeType = Objects.requireNonNull(file.getContentType());
            boolean previewEnabled = isPreviewEnabled(originalName, resumeProperties.getAllowPreviewTypes());
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
            asyncService.resumeSummarizeAndSave(userId, taskId, resumeId, content, resumeMetadata);
            // 第八步：向布隆过滤器添加数据
            if (!resumeMetadataBloomFilter.maybeExist(userId)) {
                resumeMetadataBloomFilter.addValue(userId);
            }
            // 第九步：构建响应
            Map<String, ? extends Serializable> data = Map.of("taskId", taskId, "resumeId", resumeId);

            return GenericResponse.<Map<String, ? extends Serializable>>builder().code(ResponseCode.OK.getCode())
                    .message("简历上传成功，请等待后台解析！")
                    .data(data)
                    .build();
        } catch (Exception e) {
            if (e instanceof ResumeAnalyzeAndSaveException) {
                throw new ResumeAnalyzeAndSaveException(e.getMessage());
            }
            throw new ResumeUploadException(e.getMessage());
        }
    }

    /**
     * 简历下载接口
     *
     * @param resumeIds 文件编号列表
     * @return 文件流
     */

    @Override
    public GenericResponse<?> resumeDownload(String[] resumeIds) {
        User currentUser = userContext.getUser();
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
            Integer downloadExpired = resumeProperties.getDownloadExpired();
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
        return GenericResponse.builder()
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
    public GenericResponse<?> resumeDelete(String[] resumeIds) {
        GenericResponse<?> result;
        User currentUser = userContext.getUser();
        if (resumeIds == null || resumeIds.length == 0) {
            throw new ResumeDeleteException("简历信息不能为空！");
        }
        try {
            Long userId = currentUser.getUserId();
            List<String> resumeIdList = Arrays.stream(resumeIds).toList();
            List<ResumeMetadata> resumeMetadataList = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeIdList(userId, resumeIdList);
            // 第一步：删除redis缓存（元信息+详细信息）
            String redisKey = resumeRedisKeyPrefix + userId;
            redisTemplate.delete(redisKey);
            List<String> redisKeyList = resumeIdList.stream().map(resumeId -> resumeProperties.getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId).toList();
            redisTemplate.delete(redisKeyList);
            // 第二步：删除mysql中元信息
            int affectRows = resumeMetadataMapper.deleteResumeMetadataByUserIdAndResumeId(userId, resumeIdList);
            if (affectRows == 0) {
                throw new ResumeDeleteException("删除失败，简历信息不存在！");
            }
            // 第三步：删除mongodb中的详细信息
            resumeIdList.forEach(resumeId -> resumeDetailRepository.deleteResumeDetailByUserIdAndResumeId(userId, resumeId));
            // 第四步：删除minio存储的物理文件
            for (ResumeMetadata resumeMetadata : resumeMetadataList) {
                String bucketName = resumeMetadata.getBucketName();
                String objectName = resumeMetadata.getObjectName();
                minioService.deleteFile(bucketName, objectName);
            }
            // 第五步：redis延时双删（元信息+详细信息）
            Thread.sleep(200);
            redisTemplate.delete(redisKey);
            redisTemplate.delete(redisKeyList);
            // 第六步：构建响应
            result = GenericResponse.builder()
                    .code(ResponseCode.OK.getCode())
                    .message("简历删除成功！").build();
        } catch (Exception e) {
            throw new ResumeDeleteException(e.getMessage());
        }
        return result;
    }

    /**
     * 简历展示接口，获取当前用户的所有简历元信息
     *
     * @return 简历信息列表
     */
    @Override
    public GenericResponse<?> resumeMetadataDisplay() {
        String message = "未查询到当前用户的简历信息！";
        List<ResumeMetadata> resumeMetadataList = null;
        Long userId = userContext.getUser().getUserId();

        // 第一步：查询布隆过滤器中是否存在该用户的简历，不存在直接返回
        if (resumeMetadataBloomFilter.maybeExist(userId)) {
            // 布隆过滤器存在，查redis
            log.info("布隆过滤器存在该用户信息，放行，查redis");
            String redisKey = resumeRedisKeyPrefix + userId;
            List<String> resumeMetadataStringList = redisTemplate.opsForList().range(redisKey, 0, -1);
            // redis能查到，直接返回
            if (resumeMetadataStringList != null && !resumeMetadataStringList.isEmpty()) {
                log.info("redis能查到该用户信息，直接返回");
                message = "简历信息获取成功！";
                resumeMetadataList = resumeMetadataStringList.stream()
                        .map(resumeMetadataString -> JsonUtils.toBean(resumeMetadataString, ResumeMetadata.class))
                        .toList();
            } else {
                log.info("redis不能查到该用户简历信息，查mysql");
                resumeMetadataList = resumeMetadataMapper.getResumeMetadataByUserId(userId);
                message = resumeMetadataList == null ? "未查询到当前用户的简历信息！" : "简历信息获取成功！";
                if (resumeMetadataList != null && !resumeMetadataList.isEmpty()) {
                    // 回写redis
                    log.info("mysql查询到了用户简历信息，回写redis");
                    redisTemplate.opsForList().leftPushAll(redisKey, resumeMetadataList.stream().map(JsonUtils::toJsonStr).toArray(String[]::new));
                }

            }
        }

        return GenericResponse.builder()
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
    public GenericResponse<ResumeMetadata> getResumeAsyncUploadResult(Long userId, String taskId, String resumeId) {
        ResumeMetadata resumeMetadata = resumeMetadataMapper.getResumeMetadataByUserIdAndResumeId(userId, resumeId);
        return GenericResponse.<ResumeMetadata>builder()
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
    public GenericResponse<?> resumePreview(String resumeId) {
        User currentUser = userContext.getUser();
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
        Integer expired = resumeProperties.getPreviewExpired();
        try {
            String fileUrl = minioService.getFileUrl(bucketName, objectName, expired);
            String taskId = TaskUtils.generateTaskId();
            resumeMetadata.setViewCount(resumeMetadata.getViewCount() + 1);
            resumeMetadataMapper.updateResumeMetadata(currentUser.getUserId(), taskId, resumeMetadata);
            return GenericResponse.builder().code(ResponseCode.OK.getCode())
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
    public GenericResponse<?> resumeAnalyze(String resumeId) {
        User currentUser = userContext.getUser();
        Long userId = currentUser.getUserId();
        String redisKey = resumeProperties.getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId;
        String hashKeyForAnalyze = "analyzeHtmlContent";
        String hashKeyForAnalyzeStatus = "analyzeStatus";
        String hashKeyForAnalyzeTaskId = "analyzeTaskId";
        // 第一步：查询该简历是否已经处于分析状态，如是则避免重复请求，消耗资源
        List<Object> analyzeProcessingResult = redisTemplate.opsForHash().multiGet(redisKey, List.of(hashKeyForAnalyzeStatus, hashKeyForAnalyzeTaskId));
        if (analyzeProcessingResult.size() == 2) {
            String status = (String) analyzeProcessingResult.get(0);
            String runningTaskId = (String) analyzeProcessingResult.get(1);
            if (TaskStatus.RUNNING.toString().equals(status) && runningTaskId != null) {
                return GenericResponse.builder()
                        .code(ResponseCode.ASYNC_TASK_RUNNING.getCode())
                        .data(Map.of("taskId", runningTaskId))
                        .message("简历正在分析中，请勿重复提交！")
                        .build();
            }
        }
        // 第二步：首先查询数据库中是否已经保存了该简历的解析结果，采用：Redis-mongodb-ai，3级缓存提高查询效率
        // 一级缓存：redis读取

        String analyzeResult = (String) redisTemplate.opsForHash().get(redisKey, hashKeyForAnalyze);
        if (StringUtils.hasText(analyzeResult)) {
            return GenericResponse.builder()
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
            return GenericResponse.builder()
                    .code(ResponseCode.OK.getCode())
                    .data(analyzeResult)
                    .message("简历分析结果获取成功！")
                    .build();
        }
        // 三级缓存从ai获取，触发异步任务
        String taskId = TaskUtils.generateTaskId();
        asyncService.resumeAnalyzeAndSave(userId, taskId, resumeId);
        return GenericResponse.builder()
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
    public GenericResponse<String> getResumeAsyncAnalyzeResult(Long userId, String taskId, String resumeId) {
        String redisKey = resumeProperties.getRedisKeyPrefixForAnalyze() + ":" + userId + ":" + resumeId;
        String hashKey = "analyzeHtmlContent";
        String analyzeResult = ((String) redisTemplate.opsForHash().get(redisKey, hashKey));
        if (StringUtils.hasText(analyzeResult)) {
            return GenericResponse.<String>builder()
                    .code(ResponseCode.OK.getCode())
                    .data(analyzeResult)
                    .message("简历分析完毕！")
                    .build();
        }
        String result = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId).getResumeAnalyzeHtmlContentForJobSeekers();
        return GenericResponse.<String>builder()
                .code(ResponseCode.OK.getCode())
                .data(result)
                .message("简历分析完毕！")
                .build();
    }

    /**
     * 获取简历详细信息
     *
     * @param resumeId 简历ID
     * @return 包含简历详细信息的通用响应对象
     */
    @Override
    public GenericResponse<ResumeDetail> getResumeDetail(String resumeId) {
        // 获取当前登录用户ID
        Long userId = userContext.getUser().getUserId();

        // 根据用户ID和简历ID查询简历详情
        ResumeDetail resumeDetail = resumeDetailRepository.findByUserIdAndResumeId(userId, resumeId);

        // 构建成功响应结果
        return GenericResponse.<ResumeDetail>builder()
                .code(ResponseCode.OK.getCode())
                .data(resumeDetail)
                .message("获取简历详细信息成功")
                .build();
    }
}
