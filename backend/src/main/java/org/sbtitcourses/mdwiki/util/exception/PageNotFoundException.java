package org.sbtitcourses.mdwiki.util.exception;

/**
 * Исключение "страница не найдена"
 */
public class PageNotFoundException extends RuntimeException {
    public String getMessage() {
        return "Страница не найдена";
    }
}
