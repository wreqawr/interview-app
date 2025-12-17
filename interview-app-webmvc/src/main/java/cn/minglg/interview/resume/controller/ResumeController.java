package cn.minglg.interview.resume.controller;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.response.ResponseCode;
import cn.minglg.commons.model.user.pojo.User;
import cn.minglg.interview.common.annotation.ExceptionHandler;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * ClassName:ResumeController
 * Package:cn.minglg.interview.resume
 * Description:简历功能
 *
 * @Author kfzx-minglg
 * @Create 2025/7/23
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final ResumeService resumeService;
    private final RequestScopedUserContext userContext;


    /**
     * 简历上传接口
     *
     * @param file 上传文件
     * @return 响应结果
     */
    @PostMapping("/upload")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_UPLOAD_FAIL,
            errorMessagePrefix = "简历上传失败")
    public ResponseEntity<GenericResponse<?>> resumeUpload(@RequestParam("resume") MultipartFile file) {
        GenericResponse<?> result = resumeService.resumeUpload(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历下载接口
     *
     * @param resumeIds 简历id列表
     * @return 响应结果
     */
    @GetMapping("/download")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_DOWNLOAD_FAIL,
            errorMessagePrefix = "简历下载失败")
    public ResponseEntity<GenericResponse<?>> resumeDownload(@RequestParam("resumeIds") String[] resumeIds) {
        GenericResponse<?> result = resumeService.resumeDownload(resumeIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历删除接口
     *
     * @param resumeIds 简历id列表
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_DELETE_FAIL,
            errorMessagePrefix = "简历删除失败")
    public ResponseEntity<GenericResponse<?>> resumeDelete(@RequestBody String[] resumeIds) {
        GenericResponse<?> result = resumeService.resumeDelete(resumeIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历元信息列表展示接口
     *
     * @return 简历元信息列表
     */
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/getMyResume")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_QUERY_FAIL,
            errorMessagePrefix = "简历查询失败")
    public ResponseEntity<GenericResponse<?>> resumeMetadataDisplay() {
        GenericResponse<?> result = resumeService.resumeMetadataDisplay();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历上传异步任务查询接口
     *
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 查询信息
     */
    @GetMapping("/queryResumeAsyncUploadResult/{taskId}/{resumeId}")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_SUMMARIZE_FAIL,
            errorMessagePrefix = "简历结果提取失败")
    public ResponseEntity<GenericResponse<?>> queryResumeSummarizeResult(
            @PathVariable("taskId") String taskId,
            @PathVariable("resumeId") String resumeId) {
        User currentUser = userContext.getUser();
        GenericResponse<?> result = resumeService.getResumeAsyncUploadResult(currentUser.getUserId(), taskId, resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历预览接口
     *
     * @param resumeId 简历id
     * @return 预览url地址
     */
    @GetMapping("/preview/{resumeId}")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_PREVIEW_FAIL,
            errorMessagePrefix = "简历预览失败")
    public ResponseEntity<GenericResponse<?>> getPreviewUrl(
            @PathVariable("resumeId") String resumeId) {
        GenericResponse<?> result = resumeService.resumePreview(resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历分析接口
     *
     * @param resumeId 简历id
     * @return 同步返回分析结果，异步返回taskId
     */
    @GetMapping("/analyze/{resumeId}")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_PREVIEW_FAIL,
            errorMessagePrefix = "简历分析失败")
    public ResponseEntity<GenericResponse<?>> resumeAnalyze(
            @PathVariable("resumeId") String resumeId) {
        GenericResponse<?> result = resumeService.resumeAnalyze(resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历分析异步任务查询接口
     *
     * @param taskId   任务id
     * @param resumeId 简历id
     * @return 任务信息
     */
    @GetMapping("/queryResumeAsyncAnalyzeResult/{taskId}/{resumeId}")
    @ExceptionHandler(
            errResponseCode = ResponseCode.RESUME_SUMMARIZE_FAIL,
            errorMessagePrefix = "异步获取简历分析结果失败")
    public ResponseEntity<GenericResponse<?>> queryResumeAsyncAnalyzeResult(
            @PathVariable("taskId") String taskId,
            @PathVariable("resumeId") String resumeId) {
        User currentUser = userContext.getUser();
        GenericResponse<?> result = resumeService.getResumeAsyncAnalyzeResult(currentUser.getUserId(), taskId, resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
