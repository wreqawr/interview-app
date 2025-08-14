package cn.minglg.interview.common.constant.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:ChatClientType
 * Package:cn.minglg.interview.common.constant.ai
 * Description:对话client类型
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum ChatClientType {
    /**
     * 枚举值
     */
    GENERAL_WITHOUT_MEMORY("通用chatClient，不带记忆"),
    GENERAL_WITH_MEMORY("通用chatClient，带记忆");

    private final String description;


}
