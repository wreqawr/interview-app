package cn.minglg.interview.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * ClassName:RsaKeyGeneratorConfig
 * Package:cn.minglg.interview.config
 * Description:让应用启动时生成密钥对，纳入Ioc容器管理
 *
 * @Author kfzx-minglg
 * @Create 2025/6/15
 * @Version 1.0
 */
@Configuration
public class RsaKeyGeneratorConfig {

    /**
     * 生成密钥对
     *
     * @return 密钥对
     */
    @Bean("keyPair")
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        // 密钥大小为2048位
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取Base64编码的字符串形式的公钥（PEM格式）
     *
     * @param keyPair 密钥对
     * @return Base64编码的字符串形式的公钥（PEM格式）
     */
    @Bean("publicKeyPem")
    public String getPublicKeyPem(KeyPair keyPair) {
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyPair.getPublic().getEncoded()) +
                "\n-----END PUBLIC KEY-----";
    }

    /**
     * 获取Base64编码的字符串形式的私钥（PEM格式）
     *
     * @param keyPair 密钥对
     * @return Base64编码的字符串形式的私钥（PEM格式）
     */
    @Bean("privateKeyPem")
    public String getPrivateKeyPem(KeyPair keyPair) {
        return "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyPair.getPrivate().getEncoded()) +
                "\n-----END PRIVATE KEY-----";
    }

}
