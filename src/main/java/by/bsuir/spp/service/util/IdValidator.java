package by.bsuir.spp.service.util;

import by.bsuir.spp.exception.FailedValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdValidator {

    public static void validateId(Integer id) throws FailedValidationException {
        if (id < 0)
            throw new FailedValidationException("Id must not be negative");
    }
}
