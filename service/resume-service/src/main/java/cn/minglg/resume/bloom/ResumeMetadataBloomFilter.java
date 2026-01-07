package cn.minglg.resume.bloom;

import cn.minglg.commons.bloom.BloomFilter;
import cn.minglg.resume.constants.ResumeConstants;
import cn.minglg.resume.mapper.ResumeMetadataMapper;
import cn.minglg.resume.pojo.ResumeMetadata;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * ClassName:ResumeMetadataBloomFilter
 * Package:cn.minglg.resume.bloom
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2026/1/7
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeMetadataBloomFilter implements BloomFilter<String> {
    private final RedissonClient redissonClient;
    private final ResumeMetadataMapper resumeMetadataMapper;
    // 暂时写死，后续可以放到配置文件中
    private static final String BLOOM_FILTER_NAME = ResumeConstants.RESUME_METADATA_BLOOM_FILTER_NAME;
    private static final long EXPECT_SIZE = ResumeConstants.RESUME_METADATA_BLOOM_FILTER_EXPECT_SIZE;
    private static final double ERROR_RATE = ResumeConstants.RESUME_METADATA_BLOOM_FILTER_ERROR_RATE;

    /**
     * 获取布隆过滤器实例
     *
     * @return 返回RBloomFilter类型的布隆过滤器实例
     */
    @Override
    public RBloomFilter<String> getBloomFilter() {
        return redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
    }

    /**
     * 初始化布隆过滤器
     * 该方法在Bean初始化完成后执行，用于创建布隆过滤器并进行缓存预热
     * 将数据库中所有用户的ID添加到布隆过滤器中，用于后续的快速存在性检查
     */
    @Override
    @PostConstruct
    public void initBloomFilter() {
        RBloomFilter<String> bloomFilter = getBloomFilter();
        // 初始化布隆过滤器
        if (bloomFilter.isExists()) {
            bloomFilter.delete();
        }
        bloomFilter.tryInit(EXPECT_SIZE, ERROR_RATE);
        LambdaQueryWrapper<ResumeMetadata> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(ResumeMetadata::getUserId)
                .groupBy(ResumeMetadata::getUserId);
        // 缓存预热，将用户id添加到布隆过滤器中
        resumeMetadataMapper.selectList(queryWrapper)
                .forEach(item -> bloomFilter.add(String.valueOf(item.getUserId())));
    }
}
