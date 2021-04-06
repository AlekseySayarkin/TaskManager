package by.bsuir.spp.exception;

public class FailedValidationException extends TaskManagerException {

    public FailedValidationException() {
    }

    public FailedValidationException(String message) {
        super(message);
    }

    public FailedValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedValidationException(Throwable cause) {
        super(cause);
    }
}
