package org.sbtitcourses.mdwiki.util.exception;

/**
 * Исключение "пространство не найдено"
 */
public class SpaceNotFoundException extends RuntimeException {
    public String getMessage() {
        return "Пространство не найдено";
    }
}
