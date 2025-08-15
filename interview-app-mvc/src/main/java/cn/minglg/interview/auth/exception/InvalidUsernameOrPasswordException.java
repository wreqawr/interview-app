package cn.minglg.interview.auth.exception;

/**
 * @author kfzx-minglg
 * 用户名密码不对
 */
public class InvalidUsernameOrPasswordException extends RuntimeException {
    public InvalidUsernameOrPasswordException(String message) {
        super(message);
    }
}
