package org.sbtitcourses.mdwiki.util.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Исключение "ошибка конвертации PDF".
 */
@ResponseStatus(CONFLICT)
public class PdfConversionException extends RuntimeException {

    public PdfConversionException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}