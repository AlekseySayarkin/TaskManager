package by.bsuir.spp.exception;

public class TaskManagerException extends RuntimeException {

    public TaskManagerException() {
    }

    public TaskManagerException(String message) {
        super(message);
    }

    public TaskManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskManagerException(Throwable cause) {
        super(cause);
    }
}
