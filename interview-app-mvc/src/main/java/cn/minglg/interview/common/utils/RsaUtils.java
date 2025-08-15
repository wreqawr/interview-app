package cn.minglg.interview.common.utils;

import cn.minglg.interview.auth.exception.InvalidUsernameOrPasswordException;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

/**
 * ClassName:RsaUtils
 * Package:cn.minglg.interview.utils
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/6/15
 * @Version 1.0
 */
public class RsaUtils {
    /**
     * RSA加密
     *
     * @param message   明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    /**
     * RSA解密
     *
     * @param message    密文
     * @param privateKey PEM格式公钥
     * @return 明文
     */
    public static String decrypt(String message, PrivateKey privateKey) throws Exception {
        // 创建符合前端规范的OAEP参数
        OAEPParameterSpec oaepParams = new OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT
        );

        // 使用OAEPPadding模式（而不是特定实现）
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(message));
        return new String(decryptedBytes);
    }

    public static String decrypt(String message, PrivateKey privateKey, long timeoutSeconds) throws Exception {
        String decryptMessage;
        String decryptPassword;
        long requestTimestamp;

        try {
            decryptMessage = RsaUtils.decrypt(message, privateKey);
            decryptPassword = decryptMessage.substring(0, decryptMessage.length() - 20);
            requestTimestamp = Long.parseLong(decryptMessage.substring(decryptMessage.length() - 20));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        long currentTimestamp = System.currentTimeMillis() / 1000;
        if (currentTimestamp - requestTimestamp > timeoutSeconds) {
            throw new InvalidUsernameOrPasswordException("用户名或密码不正确！");
        }
        return decryptPassword;
    }

}
