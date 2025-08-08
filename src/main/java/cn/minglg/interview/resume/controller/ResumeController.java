package cn.minglg.interview.resume.controller;

import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.annotation.ResponseEntityExceptionHandler;
import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.exception.UnKnowUserException;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.common.utils.UserUtils;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    /**
     * 简历上传接口
     *
     * @param file 上传文件
     * @return 响应结果
     */
    @PostMapping("/upload")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_UPLOAD_FAIL,
            errorMessagePrefix = "简历上传失败")
    public ResponseEntity<R> resumeUpload(@RequestParam("resume") MultipartFile file) {
        R result = resumeService.resumeUpload(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历下载接口
     *
     * @param resumeIds 简历id列表
     * @return 响应结果
     */
    @GetMapping("/download")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_DOWNLOAD_FAIL,
            errorMessagePrefix = "简历下载失败")
    public ResponseEntity<R> resumeDownload(@RequestParam("resumeIds") String[] resumeIds) {
        R result = resumeService.resumeDownload(resumeIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历删除接口
     *
     * @param resumeIds 简历id列表
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_DELETE_FAIL,
            errorMessagePrefix = "简历删除失败")
    public ResponseEntity<R> resumeDelete(@RequestBody String[] resumeIds) {
        R result = resumeService.resumeDelete(resumeIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 简历元信息列表展示接口
     *
     * @return 简历元信息列表
     */
    @GetMapping("/getMyResume")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_QUERY_FAIL,
            errorMessagePrefix = "简历查询失败")
    public ResponseEntity<R> resumeMetadataDisplay() {
        R result = resumeService.resumeMetadataDisplay();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/queryResumeAsyncUploadResult/{taskId}/{resumeId}")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_SUMMARIZE_FAIL,
            errorMessagePrefix = "简历结果提取失败")
    public ResponseEntity<R> queryResumeSummarizeResult(
            @PathVariable("taskId") String taskId,
            @PathVariable("resumeId") String resumeId) {
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效用户！");
        }
        R result = resumeService.getResumeAsyncUploadResult(currentUser.getUserId(), taskId, resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/preview/{resumeId}")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_PREVIEW_FAIL,
            errorMessagePrefix = "简历预览失败")
    public ResponseEntity<R> getPreviewUrl(
            @PathVariable("resumeId") String resumeId) {
        R result = resumeService.resumePreview(resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/analyze/{resumeId}")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_PREVIEW_FAIL,
            errorMessagePrefix = "简历分析失败")
    public ResponseEntity<R> resumeAnalyze(
            @PathVariable("resumeId") String resumeId) {
        R result = resumeService.resumeAnalyze(resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/queryResumeAsyncAnalyzeResult/{taskId}/{resumeId}")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_SUMMARIZE_FAIL,
            errorMessagePrefix = "异步获取简历分析结果失败")
    public ResponseEntity<R> queryResumeAsyncAnalyzeResult(
            @PathVariable("taskId") String taskId,
            @PathVariable("resumeId") String resumeId) {
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnKnowUserException("无效用户！");
        }
        R result = resumeService.getResumeAsyncAnalyzeResult(currentUser.getUserId(), taskId, resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
