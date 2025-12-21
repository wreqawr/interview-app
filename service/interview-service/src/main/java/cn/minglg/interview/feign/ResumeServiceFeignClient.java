package cn.minglg.interview.feign;

import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.commons.model.resume.ResumeDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ClassName:ResumeServiceFeignClient
 * Package:cn.minglg.interview.feign
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/21
 * @Version 1.0
 */
@FeignClient("resume-service")
public interface ResumeServiceFeignClient {
    @GetMapping("/api/resume/getResumeDetail/{resumeId}")
    ResponseEntity<GenericResponse<ResumeDetail>> getResumeDetail(@PathVariable("resumeId") String resumeId);
}
