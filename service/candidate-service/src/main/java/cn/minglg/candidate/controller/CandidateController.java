package cn.minglg.candidate.controller;

import cn.minglg.candidate.dto.JobDTO;
import cn.minglg.candidate.service.CandidateService;
import cn.minglg.commons.model.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:CandidateController
 * Package:cn.minglg.interview.candidate.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/30
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/candidate")
public class CandidateController {

    private final CandidateService candidateService;

    /**
     * 获取面试者投递的岗位列表
     *
     * @return ResponseEntity<R> 包含岗位列表信息的响应实体
     */
    @GetMapping("/getJobList")
    public ResponseEntity<GenericResponse<List<JobDTO>>> getCandidateList() {
        // 获取当前用户投递的岗位列表
        List<JobDTO> jobList = candidateService.getJobListByUserId();

        // 构建成功响应结果
        GenericResponse<List<JobDTO>> result = GenericResponse.<List<JobDTO>>builder()
                .code(200)
                .message("获取面试者投递的岗位成功")
                .data(jobList)
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
