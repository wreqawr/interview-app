package cn.minglg.authentication.service.impl;

import cn.minglg.authentication.properties.WebMvcSecurityProperties;
import cn.minglg.authentication.service.RsaService;
import cn.minglg.authentication.utils.RsaUtils;
import lombok.AllArgsConstructor;

import java.security.KeyPair;

/**
 * ClassName:RsaServiceImpl
 * Package:cn.minglg.authentication.service.impl
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/8/16
 * @Version 1.0
 */
@AllArgsConstructor
public class RsaServiceImpl implements RsaService {
    private final WebMvcSecurityProperties securityProperties;
    private final KeyPair keyPair;
    /**
     * 解密
     *
     * @param message 密文
     * @return 明文
     */
    @Override
    public String decrypt(String message) {
        return RsaUtils.decrypt(message, keyPair.getPrivate(), securityProperties.getRequestTimeoutSeconds());
    }
}
