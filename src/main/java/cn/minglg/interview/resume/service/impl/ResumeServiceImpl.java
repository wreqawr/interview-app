package cn.minglg.interview.resume.service.impl;

import cn.hutool.json.JSONUtil;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.properties.GlobalProperties;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.UserUtils;
import cn.minglg.interview.minio.service.MinioService;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
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
            hashValueList.add(minioService.getFileUrl(resumeUploadBucketName, newFilename));
            hashValueStr = JSONUtil.toJsonStr(hashValueList);
            redisTemplate.opsForHash().put(redisKey, hashKey, hashValueStr);

            // 第九步：构建响应
            Map<String, ? extends Serializable> message = Map.of("原始文件名", originalFilename,
                    "存储文件名", newFilename,
                    "文件类型", Objects.requireNonNull(file.getContentType()),
                    "文件大小", file.getSize());

            return R.builder().code(ResponseCode.OK.getCode())
                    .message(JSONUtil.toJsonStr(message))
                    .build();
        } catch (Exception e) {
            return R.builder().code(ResponseCode.RESUME_UPLOAD_FAIL.getCode())
                    .message("简历上传失败，原因为：" + e.getMessage())
                    .build();
        }
    }
}
