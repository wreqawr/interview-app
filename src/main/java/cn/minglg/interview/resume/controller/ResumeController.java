package cn.minglg.interview.resume.controller;

import cn.minglg.interview.common.annotation.ResponseEntityExceptionHandler;
import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
     * @param resumeName 简历名称
     * @return 响应结果
     */
    @GetMapping("/download")
    public ResponseEntity<Object> resumeDownload(@RequestParam("resumeName") String resumeName) {
        try {
            Map<String, Object> map = resumeService.resumeDownload(resumeName);
            InputStreamResource isr = (InputStreamResource) map.get("isr");
            String contentType = (String) map.get("contentType");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resumeName + "\"")
                    .body(isr);
        } catch (Exception e) {
            R result = R.builder().code(ResponseCode.RESUME_DOWNLOAD_FAIL.getCode())
                    .message("简历下载失败，原因为：" + e.getMessage())
                    .build();
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/queryResumeAsyncUploadResult")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_SUMMARIZE_FAIL,
            errorMessagePrefix = "简历分析失败")
    public ResponseEntity<R> queryResumeSummarizeResult(
            @RequestParam("taskId") String taskId,
            @RequestParam("resumeId") String resumeId) {
        R result = resumeService.getResumeAsyncUploadResult(taskId, resumeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
