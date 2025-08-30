package cn.minglg.interview.job.mapper;

import cn.minglg.interview.job.pojo.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName:JobMapper
 * Package:cn.minglg.interview.job.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/30
 * @Version 1.0
 */
@Repository
public interface JobMapper extends BaseMapper<Job> {
    /**
     * 根据用户ID获取该用户申请的岗位列表
     *
     * @param userId 用户ID
     * @return 岗位列表
     */
    @Select("""
            SELECT    tj.*,tc.company_name
            FROM      t_jobs tj
            INNER JOIN t_company tc
            ON tj.company_id=tc.company_id
            WHERE     tj.job_id IN (
                      SELECT    tcj.job_id
                      FROM      t_candidate_jobs tcj
                      WHERE     tcj.candidate_id = #{userId}
                      )
            """)
    List<Job> getJobListByUserId(@Param("userId") Long userId);
}
