package hello.itemservice.web.exception;

public class UserException extends RuntimeException {

    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }

}
