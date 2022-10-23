package org.sbtitcourses.mdwiki.util.exception;

/**
 * Исключение "человек не найден"
 */
public class PersonNotFoundException extends RuntimeException {
    public String getMessage() {
        return "Пользователь не найден";
    }
}
