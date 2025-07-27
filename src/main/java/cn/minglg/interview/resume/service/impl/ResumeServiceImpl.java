package cn.minglg.interview.resume.service.impl;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.exception.UnKnowUserException;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.UserUtils;
import cn.minglg.interview.minio.service.MinioService;
import cn.minglg.interview.resume.exception.ResumeDownloadException;
import cn.minglg.interview.resume.exception.ResumeNoPermissionDownloadException;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
    private final StringRedisTemplate redisTemplate;
    private final MinioService minioService;


    /**
     * 简历上传接口
     *
     * @param file 文件对象
     * @return 上传结果
     */
    @Override
    public R resumeUpload(MultipartFile file) {
        List<String> allowFileTypes = globalProperties.getResume().getAllowFileTypes();

        // 第一步：基础校验
        if (file.isEmpty()) {
            return R.builder()
                    .code(ResponseCode.RESUME_UPLOAD_FAIL.getCode())
                    .message("上传文件不能为空")
                    .build();
        }

        // 第二步：文件名安全处理
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // 第三步：校验文件格式（根据需要扩展）
            if (!isValidFileType(originalFilename, allowFileTypes)) {
                return R.builder()
                        .code(ResponseCode.RESUME_UPLOAD_FAIL.getCode())
                        .message("不支持的文件格式")
                        .build();
            }

            // 第四步：生成唯一文件名（防止重名和安全问题）
            String fileExtension = getFileExtension(originalFilename);
            String randomPrefix = System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
            String newFilename = randomPrefix + fileExtension;

            // 第五步：文件大小校验（自动生效于application配置）

            // 第六步：文件保存至Minio
            String resumeUploadBucketName = globalProperties.getMinio().getBucketName().get("resumeUpload");
            minioService.uploadFile(resumeUploadBucketName, file, newFilename);
            String resumeDownloadUrl = minioService.getFileUrl(resumeUploadBucketName, newFilename);
            // 第七步：用户简历元信息保存至redis
            User user = UserUtils.getCurrentUser();
            String redisKey = globalProperties.getResume().getResumeRedisKeyPrefix();
            String hashKey = "";
            if (user != null) {
                hashKey = String.valueOf(user.getUserId());
            }
            String hashValueStr = (String) redisTemplate.opsForHash().get(redisKey, hashKey);
            hashValueStr = hashValueStr == null ? "[]" : hashValueStr;
            List<String> hashValueList = JSONUtil.toList(hashValueStr, String.class);
            hashValueList.add(newFilename);
            hashValueStr = JSONUtil.toJsonStr(hashValueList);
            redisTemplate.opsForHash().put(redisKey, hashKey, hashValueStr);

            // 第八步：构建响应
            Map<String, ? extends Serializable> data = Map.of("原始文件名", originalFilename,
                    "存储文件名", newFilename,
                    "文件类型", Objects.requireNonNull(file.getContentType()),
                    "文件大小", file.getSize(),
                    "文件下载地址", resumeDownloadUrl);

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
     * @param fileName 文件名
     * @return 文件流
     */
    @Override
    public Map<String, Object> resumeDownload(String fileName) {
        User user = UserUtils.getCurrentUser();
        if (user == null) {
            throw new UnKnowUserException("无效用户！");
        }
        String redisKey = globalProperties.getResume().getResumeRedisKeyPrefix();
        String hashKey = String.valueOf(user.getUserId()) == null ? "" : String.valueOf(user.getUserId());
        List<String> hashValueList = JSONUtil.toList((String) redisTemplate.opsForHash().get(redisKey, hashKey), String.class);
        try {
            if (hashValueList == null || hashValueList.isEmpty() || !hashValueList.contains(fileName)) {
                throw new ResumeNoPermissionDownloadException("当前用户无权限下载该简历！");
            }
            String bucketName = globalProperties.getMinio().getBucketName().get("resumeUpload");
            InputStreamResource isr = new InputStreamResource(minioService.downloadFile(bucketName, fileName));
            String contentType = minioService.getContentType(bucketName, fileName);
            return Map.of("isr", isr, "contentType", contentType);

        } catch (Exception e) {
            throw new ResumeDownloadException(e.getMessage());
        }
    }

    /**
     * 简历删除接口
     *
     * @param fileName 文件名
     * @return 操作结果
     */
    @Override
    public R resumeDelete(String fileName) {
        R result;
        try {
            String bucketName = globalProperties.getMinio().getBucketName().get("resumeUpload");
            minioService.deleteFile(bucketName, fileName);
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
}
