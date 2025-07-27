package cn.minglg.interview.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ClassName:ResponseCode
 * Package:cn.minglg.interview.auth.constant
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/7/19
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    // 请求有效，且成功返回
    OK(200, "请求有效，且成功返回"),
    // JWT认证失败
    JWT_VERIFY_FAIL(201, "JWT认证失败"),
    // 验证码认证失败
    CAPTCHA_VERIFY_FAIL(202, "验证码认证失败"),
    // 退出失败
    LOGOUT_FAIL(203, "退出失败"),
    // 注册失败
    REGISTER_FAIL(204, "注册失败"),
    // 简历上传失败
    RESUME_UPLOAD_FAIL(205, "简历上传失败"),
    // 简历下载失败
    RESUME_DOWNLOAD_FAIL(206, "简历下载失败"),
    // 简历删除失败
    RESUME_DELETE_FAIL(207, "简历删除失败"),


    // 登录认证失败
    AUTH_FAIL(401, "登录认证失败"),
    // 权限不足
    PERMISSION_DENY(403, "权限不足");

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态描述
     */
    private final String description;

}
