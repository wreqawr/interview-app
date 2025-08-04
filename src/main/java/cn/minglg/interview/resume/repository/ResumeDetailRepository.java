package cn.minglg.interview.resume.repository;

import cn.minglg.interview.resume.pojo.ResumeDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * ClassName:ResumeDetailRepository
 * Package:cn.minglg.interview.resume.mapper
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/3
 * @Version 1.0
 */
public interface ResumeDetailRepository extends MongoRepository<ResumeDetail, String> {
    /**
     * 根据简历id查询简历信息
     *
     * @param resumeId 简历id
     * @return 简历信息
     */
    ResumeDetail findByResumeId(String resumeId);

    /**
     * 根据用户id以及简历id查询简历信息
     *
     * @param userId   用户id
     * @param resumeId 简历id
     * @return 简历信息
     */
    ResumeDetail findByUserIdAndResumeId(Long userId, String resumeId);

    /**
     * g根据用户id查询简历信息
     *
     * @param userId 用户id
     * @return 简历列表
     */
    List<ResumeDetail> findByUserId(Long userId);
}
