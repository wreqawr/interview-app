package cn.minglg.interview.resume.repository;

import cn.minglg.interview.resume.pojo.ResumeDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;

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
     * 根据用户id和简历id删除简历详细信息
     *
     * @param userId   用户id
     * @param resumeId 简历id
     */
    void deleteResumeDetailByUserIdAndResumeId(Long userId, String resumeId);

    /**
     * 根据userId和resumeId更新resumeAnalyzeHtmlContentForJobSeekers字段
     *
     * @param userId             用户id
     * @param resumeId           简历id
     * @param analyzeHtmlContent 字段值
     */
    @Query("{ 'userId': ?0, 'resumeId': ?1 }")
    @Update("{ '$set' : { 'resumeAnalyzeHtmlContentForJobSeekers' : ?2 } }")
    void updateResumeAnalyzeHtmlContentForJobSeekersByUserIdAndResumeId(
            @Param("userId") Long userId,
            @Param("resumeId") String resumeId,
            @Param("analyzeHtmlContent") String analyzeHtmlContent);
}
