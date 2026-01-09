package cn.minglg.resume;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ClassName:DateFormatTest
 * Package:cn.minglg.resume
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2026/1/8
 * @Version 1.0
 */
public class DateFormatTest {
    @Test
    public void test(){
        String format = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
        System.out.println(format);
    }
}
