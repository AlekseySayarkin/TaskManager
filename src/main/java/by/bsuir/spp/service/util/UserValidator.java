package by.bsuir.spp.service.util;

import by.bsuir.spp.exception.FailedValidationException;
import by.bsuir.spp.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserValidator {

    public static void validateUser(User user) throws FailedValidationException {
        if (user == null)
            throw new FailedValidationException("User must not be null");
        validateUserId(user.getId());
        validateLogin(user.getLogin());
        validatePassword(user.getPassword());
    }

    public static void validateUserId(Integer id) throws FailedValidationException {
        if (id != null)
            IdValidator.validateId(id);
    }

    public static void validateLogin(String login) throws FailedValidationException {
        if (login == null)
            throw new FailedValidationException("Login must not be null");
        if (login.isEmpty())
            throw new FailedValidationException("Login must not be empty");
        if (login.length() > 45)
            throw new FailedValidationException("Login must not be longer then 45 characters");
    }

    public static void validatePassword(String password) throws FailedValidationException {
        if (password == null)
            throw new FailedValidationException("Password must not be null");
        if (password.isEmpty())
            throw new FailedValidationException("Password must not be empty");
    }
}
