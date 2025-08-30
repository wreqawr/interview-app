package cn.minglg.interview.candidate.service;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.interview.job.mapper.JobMapper;
import cn.minglg.interview.job.pojo.Job;
import cn.minglg.interview.job.pojo.JobDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:CandidateService
 * Package:cn.minglg.interview.candidate.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/30
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class CandidateService {
    private final JobMapper jobMapper;
    private final RequestScopedUserContext userContext;

    /**
     * 根据用户ID获取该用户申请过的职位列表
     *
     * @return 职位DTO列表
     */
    public List<JobDTO> getJobListByUserId() {
        // 获取当前登录用户的用户ID
        Long userId = userContext.getUser().getUserId();

        // 调用mapper查询该用户申请过的职位
        List<Job> jobList = jobMapper.getJobListByUserId(userId);

        List<JobDTO> jobDTOList = new ArrayList<>();
        for (Job job : jobList) {
            JobDTO dto = new JobDTO();
            BeanUtils.copyProperties(job, dto);
            jobDTOList.add(dto);
        }
        return jobDTOList;
    }


}
