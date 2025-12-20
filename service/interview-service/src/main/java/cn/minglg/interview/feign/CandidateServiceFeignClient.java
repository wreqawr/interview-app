package cn.minglg.interview.feign;

import cn.minglg.commons.model.candidate.Job;
import cn.minglg.commons.model.response.GenericResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * ClassName:CandidateServiceFeignClient
 * Package:cn.minglg.resume.feign
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/19
 * @Version 1.0
 */
@FeignClient("candidate-service")
public interface CandidateServiceFeignClient {
    @GetMapping("/api/candidate/getJobDetails/{jobId}")
    ResponseEntity<GenericResponse<Job>> getJobDetails(@PathVariable("jobId") Long jobId);
}
