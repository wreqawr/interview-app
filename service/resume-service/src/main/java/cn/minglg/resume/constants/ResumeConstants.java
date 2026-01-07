package cn.minglg.resume.constants;

/**
 * ClassName:ResumeConstants
 * Package:cn.minglg.resume.constants
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2026/1/7
 * @Version 1.0
 */
public class ResumeConstants {
    public static final String RESUME_METADATA_BLOOM_FILTER_NAME = "resume-bloom-filter";
    public static final long RESUME_METADATA_BLOOM_FILTER_EXPECT_SIZE = 10000L;
    public static final double RESUME_METADATA_BLOOM_FILTER_ERROR_RATE = 0.01;
    public static final String RESUME_METADATA_REDIS_KEY_PREFIX = "candidate:resume:";
}
