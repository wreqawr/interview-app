package cn.minglg.interview.ai.core.interview;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;

/**
 * ClassName:AiInterviewCoreServiceTest
 * Package:cn.minglg.interview.ai.core.interview
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/10
 * @Version 1.0
 */
@SpringBootTest
public class AiInterviewCoreServiceTest {

    @Autowired
    private AiInterviewCoreService aiInterviewCoreService;

    @Test
    public void testMemory() {
        String conversationId = "12345";
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Q:");
            String question = scanner.nextLine();
            System.out.println("========================");
            String answer = aiInterviewCoreService.interviewOnline(conversationId, question, null);
            System.out.print("A:");
            System.out.println(answer);
            System.out.println("======================");
        }

    }

    @Test
    public void test1(){
        String a="123456";
        String b="123456789";
        System.out.println(a.substring(a.length()));
        System.out.println(b.substring(a.length()));
    }
}
