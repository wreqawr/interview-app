package cn.minglg.interview.ai.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

/**
 * ClassName:ToolServiceTest
 * Package:cn.minglg.interview.ai.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/13
 * @Version 1.0
 */
@SpringBootTest
public class ToolServiceTest {

    @Test
    public void testGetSkillMap() {
        String conversationId = "666666";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Q：");
            String userMessage = scanner.nextLine();
            System.out.println("==================");
            //String skillMap = chatService.generalChat(conversationId, userMessage);
            // System.out.println(skillMap);
            System.out.println("==================");
        }
    }
}
