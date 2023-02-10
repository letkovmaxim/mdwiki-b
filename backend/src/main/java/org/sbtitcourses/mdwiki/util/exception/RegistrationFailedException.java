package org.sbtitcourses.mdwiki.util.exception;

import java.util.List;

/**
 * Исключение "ошибка при регистрации".
 */
public class RegistrationFailedException extends RuntimeException {

    private final List<String> errors;

    public RegistrationFailedException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}