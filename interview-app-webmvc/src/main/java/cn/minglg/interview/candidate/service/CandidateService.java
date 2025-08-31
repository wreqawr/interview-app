package cn.minglg.interview.candidate.service;

import cn.minglg.authentication.context.RequestScopedUserContext;
import cn.minglg.authentication.utils.JsonUtils;
import cn.minglg.interview.job.mapper.JobMapper;
import cn.minglg.interview.job.pojo.Job;
import cn.minglg.interview.job.pojo.JobDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private final StringRedisTemplate redisTemplate;
    private final RequestScopedUserContext userContext;

    /**
     * 根据用户ID获取该用户申请过的职位列表
     *
     * @return 职位DTO列表
     */
    public List<JobDTO> getJobListByUserId() {
        // 获取当前登录用户的用户ID
        Long userId = userContext.getUser().getUserId();
        List<JobDTO> jobDTOList = new ArrayList<>();

        // 优先从缓存查询
        String redisKey = "jobs:candidate:" + userId;
        if (redisTemplate.hasKey(redisKey)) {
            Map<Object, Object> jobMaps = redisTemplate.opsForHash().entries(redisKey);
            for (Map.Entry<Object, Object> entry : jobMaps.entrySet()) {
                JobDTO dto = JsonUtils.toBean(entry.getValue().toString(), JobDTO.class);
                jobDTOList.add(dto);
            }
            return jobDTOList;
        }

        // 缓存查不到就从mysql查询
        List<Job> jobList = jobMapper.getJobListByUserId(userId);
        Map<String, String> jobMaps = new HashMap<>(16);
        for (Job job : jobList) {
            JobDTO dto = new JobDTO();
            BeanUtils.copyProperties(job, dto);
            jobDTOList.add(dto);
            jobMaps.put(String.valueOf(job.getJobId()), JsonUtils.toJsonStr(job));
        }
        // 将职位信息保存至redis中，方便后续查询
        if (!jobMaps.isEmpty()) {
            redisTemplate.opsForHash().putAll(redisKey, jobMaps);
            redisTemplate.expire(redisKey, 60, TimeUnit.MINUTES);
        }
        return jobDTOList;
    }


}
