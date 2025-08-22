package cn.minglg.authentication.exception;

/**
 * @author kfzx-minglg
 */
public class UnKnowUserException extends RuntimeException {
    public UnKnowUserException(String message) {
        super(message);
    }
}
