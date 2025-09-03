package cn.minglg.interview.resume.mapper;

import cn.minglg.commons.constant.task.TaskType;
import cn.minglg.interview.common.annotation.TaskHandler;
import cn.minglg.interview.resume.pojo.ResumeMetadata;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kfzx-minglg
 */
@Repository
public interface ResumeMetadataMapper extends BaseMapper<ResumeMetadata> {
    /**
     * 添加简历元信息
     *
     * @param resumeMetadata 简历元信息
     */
    default void addResumeMetadata(ResumeMetadata resumeMetadata) {
        insert(resumeMetadata);
    }

    /**
     * 根据用户id查询简历列表
     *
     * @param userId 用户id
     * @return 简历列表
     */
    default List<ResumeMetadata> getResumeMetadataByUserId(Long userId) {
        LambdaQueryWrapper<ResumeMetadata> wrapper = new LambdaQueryWrapper<ResumeMetadata>()
                .eq(ResumeMetadata::getUserId, userId);
        return selectList(wrapper);
    }

    /**
     * 根据用户id和简历id查询单个简历信息
     *
     * @param userId   用户id
     * @param resumeId 简历id
     * @return 简历信息
     */
    default ResumeMetadata getResumeMetadataByUserIdAndResumeId(Long userId, String resumeId) {
        LambdaQueryWrapper<ResumeMetadata> wrapper = new LambdaQueryWrapper<ResumeMetadata>()
                .eq(ResumeMetadata::getUserId, userId)
                .eq(ResumeMetadata::getResumeId, resumeId);
        return selectOne(wrapper);
    }

    /**
     * 根据用户id和简历id列表，查询多个简历信息
     *
     * @param userId       用户id
     * @param resumeIdList 简历id列表
     * @return 简历信息列表
     */
    default List<ResumeMetadata> getResumeMetadataByUserIdAndResumeIdList(Long userId, List<String> resumeIdList) {
        LambdaQueryWrapper<ResumeMetadata> wrapper = new LambdaQueryWrapper<ResumeMetadata>()
                .eq(ResumeMetadata::getUserId, userId)
                .in(ResumeMetadata::getResumeId, resumeIdList);
        return selectList(wrapper);
    }

    /**
     * 根据用户id和简历id删除简历信息
     *
     * @param userId       用户id
     * @param resumeIdList 简历id列表
     * @return 受影响的行数
     */
    default int deleteResumeMetadataByUserIdAndResumeId(Long userId, List<String> resumeIdList) {
        LambdaQueryWrapper<ResumeMetadata> wrapper = new LambdaQueryWrapper<ResumeMetadata>()
                .eq(ResumeMetadata::getUserId, userId)
                .in(ResumeMetadata::getResumeId, resumeIdList);
        return delete(wrapper);
    }

    /**
     * 更新简历元信息
     *
     * @param taskId         任务id
     * @param userId         用户id
     * @param resumeMetadata 简历元信息
     */
    @TaskHandler(taskType = TaskType.RESUME_METADATA_UPDATE)
    default void updateResumeMetadata(String taskId, Long userId, ResumeMetadata resumeMetadata) {
        LambdaUpdateWrapper<ResumeMetadata> wrapper = new LambdaUpdateWrapper<ResumeMetadata>()
                .set(ResumeMetadata::getDownloadCount, resumeMetadata.getDownloadCount())
                .set(ResumeMetadata::getViewCount, resumeMetadata.getViewCount())
                .eq(ResumeMetadata::getUserId, userId)
                .eq(ResumeMetadata::getResumeId, resumeMetadata.getResumeId());
        update(wrapper);
    }
}