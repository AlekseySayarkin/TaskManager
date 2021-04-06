package by.bsuir.spp.web.controller;

import by.bsuir.spp.exception.TaskManagerException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j
public class ExceptionController {

    @ExceptionHandler(TaskManagerException.class)
    public ResponseEntity<TaskManagerException> handleTaskManagerException(TaskManagerException exception) {
        log.error(exception.getMessage());
        exception.printStackTrace();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Exception> handleException(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
