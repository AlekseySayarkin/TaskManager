package by.bsuir.spp.service.util;

import by.bsuir.spp.exception.FailedValidationException;
import by.bsuir.spp.model.Task;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskValidator {

    public static void validateTask(Task task) throws FailedValidationException {
        if (task == null)
            throw new FailedValidationException("Task must not be null");
        validateTaskId(task.getId());
        validateTask(task.getTask());
        validateEndDate(task.getEndDate());
    }

    public static void validateTaskId(Integer id) throws FailedValidationException {
        if (id != null)
            IdValidator.validateId(id);
    }

    public static void validateTask(String task) throws FailedValidationException {
        if (task == null)
            throw new FailedValidationException("Task must not be null");
        if (task.isEmpty())
            throw new FailedValidationException("Task must not be empty");
        if (task.length() > 100)
            throw new FailedValidationException("Task must not be longer then 100 characters");
    }

    public static void validateEndDate(ZonedDateTime endDate) throws FailedValidationException {
        if (endDate == null)
            throw new FailedValidationException("EndDate must not be null");
    }
}
