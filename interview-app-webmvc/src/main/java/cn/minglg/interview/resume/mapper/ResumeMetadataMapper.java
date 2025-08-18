package cn.minglg.interview.resume.mapper;

import cn.minglg.interview.common.annotation.TaskHandler;
import cn.minglg.interview.common.constant.task.TaskType;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kfzx-minglg
 */
public interface ResumeMetadataMapper {
    /**
     * 添加简历元信息
     *
     * @param resumeMetadata 简历元信息
     */
    void addResumeMetadata(@Param("resumeMetadata") ResumeMetadata resumeMetadata);

    /**
     * 根据用户id查询简历列表
     *
     * @param userId 用户id
     * @return 简历列表
     */
    List<ResumeMetadata> getResumeMetadataByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id和简历id查询单个简历信息
     *
     * @param userId   用户id
     * @param resumeId 简历id
     * @return 简历信息
     */
    ResumeMetadata getResumeMetadataByUserIdAndResumeId(@Param("userId") Long userId, @Param("resumeId") String resumeId);

    /**
     * 根据用户id和简历id列表，查询多个简历信息
     *
     * @param userId       用户id
     * @param resumeIdList 简历id列表
     * @return 简历信息列表
     */
    List<ResumeMetadata> getResumeMetadataByUserIdAndResumeIdList(@Param("userId") Long userId, @Param("resumeIdList") List<String> resumeIdList);

    /**
     * 根据用户id和简历id删除简历信息
     *
     * @param userId       用户id
     * @param resumeIdList 简历id列表
     * @return 受影响的行数
     */
    int deleteResumeMetadataByUserIdAndResumeId(@Param("userId") Long userId, @Param("resumeIdList") List<String> resumeIdList);

    /**
     * 更新简历元信息
     *
     * @param userId         用户id
     * @param taskId         任务id
     * @param resumeMetadata 简历元信息
     */
    @TaskHandler(taskType = TaskType.RESUME_METADATA_UPDATE)
    void updateResumeMetadata(Long userId, String taskId, @Param("resumeMetadata") ResumeMetadata resumeMetadata);
}