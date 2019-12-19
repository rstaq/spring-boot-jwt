package at.wero.spring.auth.error;

public class UsernameExistException extends RuntimeException {
    public UsernameExistException(String msg) {
        super(msg);
    }

    public UsernameExistException(String msg, Throwable t) {
        super(msg, t);
    }
}
