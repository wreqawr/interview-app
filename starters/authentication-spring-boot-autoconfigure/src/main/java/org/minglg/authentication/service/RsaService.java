package org.minglg.authentication.service;

/**
 * ClassName:RsaService
 * Package:cn.minglg.authentication.service
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
public interface RsaService {
    /**
     * 解密
     *
     * @param message 密文
     * @return 明文
     */
    String decrypt(String message);
}
