package cn.minglg.interview.minio.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName:MinioService
 * Package:cn.minglg.interview.minio.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/27
 * @Version 1.0
 */
public interface MinioService {

    /**
     * 初始化桶
     *
     * @param bucketName 桶名
     * @throws Exception 异常
     */
    void createBucketIfNotExist(String bucketName) throws Exception;

    /**
     * 删除桶
     *
     * @param bucketName 桶名
     * @throws Exception 异常
     */
    void deleteBucketIfExist(String bucketName) throws Exception;

    /**
     * 文件上传
     *
     * @param bucketName     桶名称
     * @param originFile     原始文件（File对象）
     * @param targetFileName 目标文件名
     * @throws Exception 异常
     */
    void uploadFile(String bucketName, MultipartFile originFile, String targetFileName) throws Exception;

    /**
     * 获取文件访问url
     *
     * @param bucketName 桶名
     * @param fileName   文件名
     * @return 文件url
     * @throws Exception 异常
     */
    String getFileUrl(String bucketName, String fileName) throws Exception;

    /**
     * 文件删除
     *
     * @param bucketName 桶名称
     * @param fileName   文件名
     * @throws Exception 异常
     */
    void deleteFile(String bucketName, String fileName) throws Exception;

    /**
     * 文件下载
     *
     * @param bucketName 桶名称
     * @param fileName   文件名
     */
    void downloadFile(String bucketName, String fileName);

}
