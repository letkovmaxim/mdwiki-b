package org.sbtitcourses.mdwiki.util.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

/**
 * Исключение "недопустимый тип файла".
 */
@ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedTypeException extends RuntimeException {

    public UnsupportedTypeException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}