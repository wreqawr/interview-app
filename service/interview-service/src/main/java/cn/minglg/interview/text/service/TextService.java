package cn.minglg.interview.text.service;

import cn.minglg.commons.model.candidate.Job;
import cn.minglg.commons.model.response.GenericResponse;
import cn.minglg.interview.feign.CandidateServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * ClassName:TextService
 * Package:cn.minglg.interview.text.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class TextService {
    private final CandidateServiceFeignClient candidateServiceFeignClient;

    public ResponseEntity<GenericResponse<Job>> testFeignClient(Long jobId) {
        return candidateServiceFeignClient.getJobDetails(jobId);
    }
}
