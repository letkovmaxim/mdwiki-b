package org.sbtitcourses.mdwiki.dto.error;

import java.time.Instant;
import java.util.List;

/**
 * DTO для ответа об ошибках.
 */
public class ErrorResponse {

    /**
     * Сообщение об ошибке.
     */
    private final String message;

    /**
     * Время отправки сообщения об ошибке.
     */
    private final Instant timestamp;

    /**
     * Список ошибок.
     */
    private final List<String> errors;

    /**
     * Конструктор для создания объекта исключения.
     *
     * @param message   сообщение об ошибке.
     * @param timestamp время отправки сообщения об ошибке.
     * @param errors    список ошибок.
     */
    public ErrorResponse(String message, Instant timestamp, List<String> errors) {
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }
}
