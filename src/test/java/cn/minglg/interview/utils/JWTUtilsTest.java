package cn.minglg.interview.utils;

import cn.minglg.interview.auth.mapper.UserMapper;
import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.common.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;

/**
 * ClassName:JWTUtilsTest
 * Package:cn.minglg.interview.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@SpringBootTest
public class JWTUtilsTest {

    User user = User.builder().userId(111L).username("张三").password("123456").build();
    @Autowired
    private KeyPair keyPair;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test1() {
        // 创建JWT
        String token = JwtUtils.createJwt(user, 100000, keyPair);
        System.out.println(token);

        User result = JwtUtils.verifyJwt(token, keyPair);
        System.out.println(result);
    }

    @Test
    public void test2() {
        User silence = userMapper.getUserWithDetailsByUserName("silence");
        System.out.println("=========================");
        System.out.println(silence);
        String token = JwtUtils.createJwt(silence, 100, keyPair);
        System.out.println("=========================");
        System.out.println(token);
        System.out.println("=========================");
        System.out.println(JwtUtils.verifyJwt(token, keyPair));
    }
}
