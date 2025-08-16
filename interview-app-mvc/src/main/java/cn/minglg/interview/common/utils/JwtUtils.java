package cn.minglg.interview.common.utils;

import cn.minglg.interview.auth.pojo.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

/**
 * ClassName:JwtUtils
 * Package:cn.minglg.interview.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/16
 * @Version 1.0
 */
public class JwtUtils {

    /**
     * 创建JWT令牌
     *
     * @param user       用户对象，将被序列化并存储在令牌的claims中
     * @param expiration 令牌有效期，单位为分钟
     * @param secret     用于签名的密钥
     * @return 生成的JWT令牌字符串
     * @throws JWTCreationException 当JWT令牌创建失败时抛出此异常
     */
    public static String createJwt(User user, long expiration, String secret) {
        // 将用户对象转换为JSON字符串
        String userJson = JsonUtils.toJsonStr(user);
        try {
            // 使用HMAC256算法和密钥创建签名算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 构建并返回JWT令牌
            return JWT.create()
                    // 设置签发者
                    .withIssuer("2820996063@qq.com")
                    // 设置签发时间
                    .withIssuedAt(new Date())
                    // 设置过期时间
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                    // 添加用户信息声明
                    .withClaim("claims", userJson)
                    // 使用算法签名并生成令牌
                    .sign(algorithm);
        } catch (Exception e) {
            throw new JWTCreationException("JWT令牌创建失败！" + e.getMessage(), e);
        }
    }


    public static User verifyJwt(String token, String secret) {
        DecodedJWT decoder;
        String claims;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("2820996063@qq.com")
                    .build();

            decoder = verifier.verify(token);
            claims = decoder.getClaim("claims").asString();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT认证失败：" + e.getMessage(), e);
        }
        return JsonUtils.toBean(claims, User.class);
    }
}
