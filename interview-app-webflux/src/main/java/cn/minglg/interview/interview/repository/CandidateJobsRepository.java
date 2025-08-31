package cn.minglg.interview.interview.repository;

import cn.minglg.interview.interview.pojo.Job;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * ClassName:CandidateJobsRepository
 * Package:cn.minglg.interview.interview.repository
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/31
 * @Version 1.0
 */
@Repository
public interface CandidateJobsRepository extends R2dbcRepository<Job, Long> {
    /**
     * 根据用户id查询用户收藏的职位
     *
     * @param userId 用户id
     * @return Flux<Job>
     */
    @Query("""
            SELECT    tj.*,tc.company_name
            FROM      t_jobs tj
            INNER JOIN t_company tc
            ON tj.company_id=tc.company_id
            WHERE     tj.job_id =?
            """)
    Mono<Job> findJobDetailsByJobId(Long userId);
}
