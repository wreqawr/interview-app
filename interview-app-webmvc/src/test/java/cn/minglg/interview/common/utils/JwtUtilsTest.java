package cn.minglg.interview.common.utils;

import cn.minglg.authentication.pojo.User;
import cn.minglg.authentication.utils.JwtUtils;
import cn.minglg.interview.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * ClassName:JwtUtilsTest
 * Package:cn.minglg.interview.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/18
 * @Version 1.0
 */
@SpringBootTest
public class JwtUtilsTest {

    String secret = "123456";
    @Autowired
    private UserMapper userMapper;


    @Test
    public void test2() {
        User silence = userMapper.getUserWithDetailsByUserName("silence");
        System.out.println("=========================");
        System.out.println(silence);
        String token = JwtUtils.createJwt(silence, 100, secret);
        System.out.println("=========================");
        System.out.println(token);
        System.out.println("=========================");
        System.out.println(JwtUtils.verifyJwt(token, secret));
    }

    @Test
    public void test3() {
        User silence = userMapper.getUserWithDetailsByUserName("silence");
        System.out.println("======================");
        System.out.println(silence);
        String token = JwtUtils.createJwt(silence, 1000, secret);
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIyODIwOTk2MDYzQHFxLmNvbSIsImlhdCI6MTc1NTMxMzk3NiwiZXhwIjoxNzU1MzE1Nzc2LCJjbGFpbXMiOiJ7XCJAY2xhc3NcIjpcImNuLm1pbmdsZy5pbnRlcnZpZXcuYXV0aC5wb2pvLlVzZXJfJCRfanZzdDIzMl8wXCIsXCJ1c2VySWRcIjo2LFwidXNlcm5hbWVcIjpcInNpbGVuY2VcIixcIm5pY2tuYW1lXCI6XCJzaWxlbmNlXCIsXCJlbWFpbFwiOlwiMTIzNDU2QHFxLmNvbVwiLFwic3RhdHVzXCI6XCJOT1JNQUxcIixcImNvbXBhbnlcIjp7XCJAY2xhc3NcIjpcImNuLm1pbmdsZy5pbnRlcnZpZXcuYXV0aC5wb2pvLkNvbXBhbnlcIixcImNvbXBhbnlOYW1lXCI6XCJHb29nbGVcIn0sXCJyb2xlc1wiOltcImphdmEudXRpbC5BcnJheUxpc3RcIixbe1wiQGNsYXNzXCI6XCJjbi5taW5nbGcuaW50ZXJ2aWV3LmF1dGgucG9qby5Sb2xlXyQkX2p2c3QyMzJfMVwiLFwicm9sZUlkXCI6MyxcInJvbGVOYW1lXCI6XCJST0xFX0hSXCIsXCJkZXNjcmlwdGlvblwiOlwi5LyB5Lia5oub6IGY5Lq65ZGYXCIsXCJoYW5kbGVyXCI6e1wiQGNsYXNzXCI6XCJvcmcuYXBhY2hlLmliYXRpcy5leGVjdXRvci5sb2FkZXIuamF2YXNzaXN0LkphdmFzc2lzdFByb3h5RmFjdG9yeSRFbmhhbmNlZFJlc3VsdE9iamVjdFByb3h5SW1wbFwifX1dXSxcImVuYWJsZWRcIjp0cnVlLFwiYWNjb3VudE5vbkV4cGlyZWRcIjp0cnVlLFwiYWNjb3VudE5vbkxvY2tlZFwiOnRydWUsXCJjcmVkZW50aWFsc05vbkV4cGlyZWRcIjp0cnVlLFwiYXV0aG9yaXRpZXNcIjpbXCJqYXZhLnV0aWwuQXJyYXlMaXN0XCIsW3tcIkBjbGFzc1wiOlwib3JnLnNwcmluZ2ZyYW1ld29yay5zZWN1cml0eS5jb3JlLmF1dGhvcml0eS5TaW1wbGVHcmFudGVkQXV0aG9yaXR5XCIsXCJhdXRob3JpdHlcIjpcIlJPTEVfSFJcIn1dXX0ifQ.bYZ-OWri43BNhZI1RCUGB34vrnw5PuKububy5lMoSIE";
        User verified = JwtUtils.verifyJwt(token, secret);
        System.out.println(verified);

    }
}
