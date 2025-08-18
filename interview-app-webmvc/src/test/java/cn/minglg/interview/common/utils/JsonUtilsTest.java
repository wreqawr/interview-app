package cn.minglg.interview.common.utils;

import cn.minglg.authentication.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * ClassName:JsonUtilsTest
 * Package:cn.minglg.interview.common.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/15
 * @Version 1.0
 */
public class JsonUtilsTest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class People {
        private String name;
        private int age;
        private Address address;
    }

    @Data
    @AllArgsConstructor
    static class Address {
        private String city;
        private String street;
    }

    @Test
    public void testToJson() {
        Address address = new Address(null, "上海路");
        People people = People.builder().name("张三").age(20).address(address).build();
        String jsonStr = JsonUtils.toJsonStr(people);
        System.out.println(jsonStr);
    }

    @Test
    public void testToBean() {
        String jsonStr = """
                {
                    "name":"李四",
                    "age":25
                }
                """;
        People people = JsonUtils.toBean(jsonStr, People.class);
        System.out.println(people);
    }

    @Test
    public void testToList() {
        String jsonStr = """
                [{
                    "name":"李四",
                    "age":25
                },
                {
                    "name":"王五",
                    "age":30
                }]
                """;
        List<People> peopleList = JsonUtils.toList(jsonStr, People.class);
        peopleList.forEach(System.out::println);
    }

    @Test
    public void testToMap() {
        String jsonStr = """
                {
                    "name":"王五",
                    "age":30
                }
                """;
        Map<?, ?> map = JsonUtils.toMap(jsonStr, String.class, String.class);
        System.out.println(map);
    }
}
