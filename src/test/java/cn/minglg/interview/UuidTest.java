package cn.minglg.interview;

import cn.minglg.interview.common.constant.UserRole;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName:cn.minglg.interview.UuidTest
 * Package:PACKAGE_NAME
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/14
 * @Version 1.0
 */
@SpringBootTest
public class UuidTest {
    @Test
    public void test1() {
        for (int i = 0; i < 10; i++) {
            UUID uuid = UUID.randomUUID();
            System.out.println(uuid);
        }

    }

    @Test
    public void test2() {
        String decryptMessage = "admin12300000000001750054757";
        String decryptPassword = decryptMessage.substring(0, decryptMessage.length() - 20);
        long decryptTimestamp = Long.parseLong(decryptMessage.substring(decryptMessage.length() - 20));
        System.out.println(decryptTimestamp);
        System.out.println(decryptPassword);
        long timestamp = System.currentTimeMillis() / 1000;
        System.out.println(timestamp);
    }

    @Autowired
    KeyPair keyPair;

    @Test
    public void testEncode() {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("userId", 1);
        claims.put("userName", "admin");
        claims.put("role", UserRole.ROLE_ADMIN);
        int expiration = 300;
        String token = "";
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            token = JWT.create()
                    .withIssuer("2820996063@qq.com")
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 1000))
                    .withClaim("claims", claims)
                    .sign(algorithm);
            System.out.println(token);
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        // 解密
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    //.withIssuer("2820996063@qq.com")
                    // reusable verifier instance
                    .build();

            decodedJWT = verifier.verify(token);
            Map<String, Object> decryptClaim = decodedJWT.getClaim("claims").asMap();
            System.out.println(decryptClaim);
        } catch (JWTVerificationException exception) {
            // Invalid signature/claims
            exception.printStackTrace();
        }


    }

    @Test
    public void testDecode() {
        String role = "ROLE_ADMIN";
        UserRole userRole = UserRole.valueOf(role);
        System.out.println(userRole.getDisplayName());
    }

}
