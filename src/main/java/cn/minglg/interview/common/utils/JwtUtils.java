package cn.minglg.interview.common.utils;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import cn.minglg.interview.auth.pojo.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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
    private static final JSONConfig JSON_CONFIG = new JSONConfig();

    static {
        JSON_CONFIG.setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 生成JWT令牌
     *
     * @param user       用户必要信息（不含敏感信息，减少后续数据库查询）
     * @param expiration 超时时间（单位：分）
     * @param keyPair    密钥对
     * @return 加密后的access token
     */
    public static String createJwt(User user, long expiration, KeyPair keyPair) {
        String userJson = JSONUtil.toJsonStr(user, JSON_CONFIG);
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            return JWT.create()
                    .withIssuer("2820996063@qq.com")
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                    .withClaim("claims", userJson)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new JWTCreationException("JWT令牌创建失败！" + e.getMessage(), e);
        }
    }

    public static User verifyJwt(String token, KeyPair keyPair) {
        DecodedJWT decoder;
        String claims;
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("2820996063@qq.com")
                    .build();

            decoder = verifier.verify(token);
            claims = decoder.getClaim("claims").asString();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("JWT认证失败：" + e.getMessage(), e);
        }
        return JSONUtil.toBean(claims, JSON_CONFIG, User.class);
    }
}
