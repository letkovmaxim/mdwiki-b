package org.sbtitcourses.mdwiki.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Исключение "ошибка при регистрации"
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class RegistrationFailedException extends RuntimeException {
    private final List<String> errors;

    public RegistrationFailedException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
