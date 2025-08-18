package cn.minglg.interview.minio.service.impl;

import cn.minglg.interview.minio.service.MinioService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * ClassName:MinioServiceImpl
 * Package:cn.minglg.interview.minio.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/27
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    /**
     * 初始化桶
     *
     * @param bucketName 桶名
     */
    @Override
    public void createBucketIfNotExist(String bucketName) throws Exception {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean bucketExists = minioClient.bucketExists(bucketExistsArgs);
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName 桶名称
     */
    @Override
    public void deleteBucketIfExist(String bucketName) throws Exception {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        boolean bucketExists = minioClient.bucketExists(bucketExistsArgs);
        if (bucketExists) {
            deleteAllFiles(bucketName);
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 文件上传
     *
     * @param bucketName     桶名称
     * @param originFile     原始文件（File对象）
     * @param targetFileName 目标文件名
     */
    @Override
    public void uploadFile(String bucketName, MultipartFile originFile, String targetFileName) throws Exception {
        this.createBucketIfNotExist(bucketName);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(targetFileName)
                .stream(originFile.getInputStream(), originFile.getSize(), -1)
                .contentType(originFile.getContentType())
                .build();
        minioClient.putObject(putObjectArgs);
    }

    /**
     * 生成短时效预签名URL
     *
     * @param bucketName 桶名
     * @param fileName   文件名
     * @param expired    有效时间
     * @return 文件url
     * @throws Exception 异常
     */
    @Override
    public String getFileUrl(String bucketName, String fileName, Integer expired) throws Exception {
        GetPresignedObjectUrlArgs objectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .method(Method.GET)
                .expiry(expired)
                .build();
        return minioClient.getPresignedObjectUrl(objectUrlArgs);
    }

    /**
     * 文件删除
     *
     * @param bucketName 桶名称
     * @param fileName   文件名
     */
    @Override
    public void deleteFile(String bucketName, String fileName) throws Exception {
        RemoveObjectArgs objectArgs = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        minioClient.removeObject(objectArgs);
    }

    /**
     * 文件下载
     *
     * @param bucketName 桶名称
     * @param fileName   文件名
     * @return 文件流
     * @throws Exception 异常
     */
    @Override
    public InputStream downloadFile(String bucketName, String fileName) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        return minioClient.getObject(objectArgs);
    }

    private void deleteAllFiles(String bucketName) throws Exception {
        // 列出所有对象
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .recursive(true)
                        .build());

        for (Result<Item> result : results) {
            Item item = result.get();
            deleteFile(bucketName, item.objectName());
        }
    }
}
