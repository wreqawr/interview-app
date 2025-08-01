package cn.minglg.interview.resume.controller;

import cn.minglg.interview.common.constant.ResponseCode;
import cn.minglg.interview.common.response.R;
import cn.minglg.interview.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<R> resumeUpload(@RequestParam("resume") MultipartFile file) {
        return new ResponseEntity<>(resumeService.resumeUpload(file), HttpStatus.OK);
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

    @DeleteMapping("/delete")
    public ResponseEntity<R> resumeDelete(@RequestBody String[] resumeIds) {
        return new ResponseEntity<>(resumeService.resumeDelete(resumeIds), HttpStatus.OK);
    }
}
