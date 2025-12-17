package cn.minglg.interview.ai.core.resume;

import cn.minglg.commons.model.task.TaskStatus;
import cn.minglg.interview.common.mapper.TaskMapper;
import cn.minglg.interview.common.pojo.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.UUID;

/**
 * ClassName:AiResumeCoreServiceTest
 * Package:cn.minglg.interview.ai.core.resume
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/22
 * @Version 1.0
 */
@SpringBootTest
@TestPropertySource(properties = {
        "logging.level.cn.minglg.interview.common.aspects.TaskAspect=DEBUG",
        "logging.level.org.springframework.aop=DEBUG"
})
public class AiResumeCoreServiceTest {
    @Autowired
    private AiResumeCoreService aiResumeCoreService;

    @Autowired
    private TaskMapper taskMapper;

    @Test
    public void testResumeAnalyzeAndSave() {
        System.out.println("=== 开始测试 resumeAnalyzeAndSave 方法 ===");
        System.out.println("当前线程: " + Thread.currentThread().getName());

        // 使用随机UUID作为taskId，避免数据库主键冲突
        String randomTaskId = UUID.randomUUID().toString();
        System.out.println("使用随机taskId: " + randomTaskId);

        try {
            System.out.println("调用前的时间戳: " + System.currentTimeMillis());
            aiResumeCoreService.resumeAnalyzeAndSave(randomTaskId, "17558698362994e9fb96973184ff");
            System.out.println("调用后的时间戳: " + System.currentTimeMillis());
            System.out.println("=== 测试完成 ===");
        } catch (Exception e) {
            System.err.println("测试执行异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testResumeAnalyzeAndSaveWithWait() {
        System.out.println("=== 开始测试 resumeAnalyzeAndSave 方法（等待异步任务完成） ===");
        System.out.println("当前线程: " + Thread.currentThread().getName());

        // 使用随机UUID作为taskId，避免数据库主键冲突
        String randomTaskId = UUID.randomUUID().toString();
        System.out.println("使用随机taskId: " + randomTaskId);

        try {
            System.out.println("调用前的时间戳: " + System.currentTimeMillis());
            aiResumeCoreService.resumeAnalyzeAndSave(randomTaskId, "17558698362994e9fb96973184ff");
            System.out.println("调用后的时间戳: " + System.currentTimeMillis());

            // 等待异步任务完成
            System.out.println("等待异步任务完成...");
            boolean taskCompleted = false;
            int maxWaitSeconds = 60; // 最多等待60秒

            for (int i = 0; i < maxWaitSeconds; i++) {
                Thread.sleep(1000); // 等待1秒

                // 查询任务状态
                Task task = taskMapper.selectById(randomTaskId);
                if (task != null) {
                    System.out.println("第" + (i + 1) + "秒 - 任务状态: " + task.getTaskStatus());

                    if (TaskStatus.FINISHED.equals(task.getTaskStatus()) ||
                            TaskStatus.FAILED.equals(task.getTaskStatus())) {
                        taskCompleted = true;
                        System.out.println("任务已完成，最终状态: " + task.getTaskStatus());
                        if (task.getErrorMessage() != null) {
                            System.out.println("错误信息: " + task.getErrorMessage());
                        }
                        break;
                    }
                } else {
                    System.out.println("第" + (i + 1) + "秒 - 未找到任务记录");
                }
            }

            if (!taskCompleted) {
                System.out.println("警告: 任务在" + maxWaitSeconds + "秒内未完成");
            }

            System.out.println("=== 测试完成 ===");
        } catch (Exception e) {
            System.err.println("测试执行异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testResumeAnalyzeAndSaveWithTimeout() {
        System.out.println("=== 开始测试 resumeAnalyzeAndSave 方法（带超时） ===");
        System.out.println("当前线程: " + Thread.currentThread().getName());

        // 使用随机UUID作为taskId，避免数据库主键冲突
        String randomTaskId = UUID.randomUUID().toString();
        System.out.println("使用随机taskId: " + randomTaskId);

        try {
            System.out.println("调用前的时间戳: " + System.currentTimeMillis());
            aiResumeCoreService.resumeAnalyzeAndSave(randomTaskId, "17558698362994e9fb96973184ff");
            System.out.println("调用后的时间戳: " + System.currentTimeMillis());
            System.out.println("=== 测试完成 ===");
        } catch (Exception e) {
            System.err.println("测试执行异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testResumeAnalyzeAndSaveDirect() throws IOException {
        System.out.println("=== 开始测试 resumeAnalyzeAndSave 方法（直接调用） ===");
        System.out.println("当前线程: " + Thread.currentThread().getName());

        // 使用随机UUID作为taskId，避免数据库主键冲突
        String randomTaskId = UUID.randomUUID().toString();
        System.out.println("使用随机taskId: " + randomTaskId);

        try {
            System.out.println("调用前的时间戳: " + System.currentTimeMillis());
            aiResumeCoreService.resumeAnalyzeAndSave(randomTaskId, "17558698362994e9fb96973184ff");
            System.out.println("调用后的时间戳: " + System.currentTimeMillis());
            System.out.println("=== 测试完成 ===");
        } catch (Exception e) {
            System.err.println("测试执行异常: " + e.getMessage());
            e.printStackTrace();
        }
        System.in.read();
    }
}
