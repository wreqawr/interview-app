package cn.minglg.interview.auth.controller;

import cn.minglg.interview.auth.pojo.User;
import cn.minglg.interview.auth.service.UserService;
import cn.minglg.interview.common.annotation.ResponseEntityExceptionHandler;
import cn.minglg.interview.common.constant.response.ResponseCode;
import cn.minglg.interview.common.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName:UserController
 * Package:cn.minglg.interview.controller
 * Description: 处理用户
 *
 * @Author kfzx-minglg
 * @Create 2025/6/13
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final String publicKeyPem;
    private final UserService userService;

    @GetMapping("/publicKey")
    public ResponseEntity<R> getPublicKey() {
        R result = R.builder().code(ResponseCode.OK.getCode())
                .data(publicKeyPem)
                .message("获取公钥成功")
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册结果通知
     */
    @PostMapping("/register")
    @ResponseEntityExceptionHandler(
            errResponseCode = ResponseCode.RESUME_UPLOAD_FAIL,
            errorMessagePrefix = "用户注册失败")
    public ResponseEntity<R> register(@RequestBody User user) {
        /*
          必填项：
            用户名、密码、邮箱、角色
          选填项：
            昵称、公司名称
          默认项：
            状态、注册时间
         */
        R result = userService.register(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
