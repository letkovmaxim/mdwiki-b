package org.sbtitcourses.mdwiki.util.exception;

/**
 * Исключение "документ не найден"
 */
public class DocumentNotFoundException extends RuntimeException {
    public String getMessage() {
        return "Документ не найден";
    }
}