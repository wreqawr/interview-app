package cn.minglg.candidate.service;

import cn.minglg.candidate.dto.JobDTO;
import cn.minglg.candidate.mapper.JobMapper;
import cn.minglg.commons.model.candidate.Job;
import cn.minglg.commons.model.context.RequestScopedUserContext;
import cn.minglg.commons.utils.JsonUtils;
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
     * 根据用户ID获取职位列表
     *
     * @return 返回该用户对应的职位列表
     */
    private List<Job> getJobListByUserId() {
        // 获取当前登录用户的用户ID
        Long userId = userContext.getUser().getUserId();
        List<Job> jobList = new ArrayList<>();

        // 优先从缓存查询
        String redisKey = "jobs:candidate:" + userId;
        if (redisTemplate.hasKey(redisKey)) {
            Map<Object, Object> jobMaps = redisTemplate.opsForHash().entries(redisKey);
            for (Map.Entry<Object, Object> entry : jobMaps.entrySet()) {
                Job job = JsonUtils.toBean(entry.getValue().toString(), Job.class);
                jobList.add(job);
            }
            return jobList;
        }

        // 缓存查不到就从mysql查询
        jobList = jobMapper.getJobListByUserId(userId);
        Map<String, String> jobMaps = new HashMap<>(16);
        for (Job job : jobList) {
            jobMaps.put(String.valueOf(job.getJobId()), JsonUtils.toJsonStr(job));
        }
        // 将职位信息保存至redis中，方便后续查询
        if (!jobMaps.isEmpty()) {
            redisTemplate.opsForHash().putAll(redisKey, jobMaps);
            redisTemplate.expire(redisKey, 60, TimeUnit.MINUTES);
        }
        return jobList;
    }


    /**
     * 根据用户ID获取职位列表的DTO对象
     *
     * @return 返回职位DTO对象列表
     */
    public List<JobDTO> getJobListDtoByUserId() {
        // 获取当前用户的职位列表
        List<Job> jobList = getJobListByUserId();
        List<JobDTO> jobDTOList = new ArrayList<>();

        // 将Job实体对象转换为JobDTO对象
        jobList.forEach(job -> {
            JobDTO jobDTO = new JobDTO();
            BeanUtils.copyProperties(job, jobDTO);
            jobDTOList.add(jobDTO);
        });

        return jobDTOList;
    }


    /**
     * 根据用户ID和作业ID获取作业详情
     * @param jobId 作业ID
     * @return 返回匹配的作业对象，如果未找到则返回null
     */
    public Job getJobDetailByUserIdAndJobId(Long jobId) {
        // 获取当前用户的作业列表
        List<Job> jobList = getJobListByUserId();

        // 遍历作业列表查找指定ID的作业
        for (Job job : jobList) {
            if (job.getJobId().equals(jobId)) {
                return job;
            }
        }
        return null;
    }



}
