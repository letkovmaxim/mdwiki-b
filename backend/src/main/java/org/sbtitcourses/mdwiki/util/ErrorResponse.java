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
     * Конструктор для инициализации полей ошибки
     * @param errors текст ошибок
     */
    public ErrorResponse(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
