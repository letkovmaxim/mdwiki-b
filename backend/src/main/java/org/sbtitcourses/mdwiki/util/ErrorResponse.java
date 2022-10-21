package org.sbtitcourses.mdwiki.util;

import java.util.Date;
import java.util.List;

/**
 * DTO класс для сообщения об ошибке
 */
public class ErrorResponse {

    /**
     * Текст ошибки
     */
    private List<String> errors;

    /**
     * Точное время получения ошибки
     */
    private Date timestamp;

    /**
     * Конструктор для инициализации полей ошибки
     * @param errors текст ошибок
     * @param timestamp точное время получения ошибки
     */
    public ErrorResponse(List<String> errors, Date timestamp) {
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
