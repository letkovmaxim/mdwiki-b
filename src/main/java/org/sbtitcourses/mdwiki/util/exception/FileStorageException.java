package org.sbtitcourses.mdwiki.util.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Исключение "ошибка записи файла".
 */
@ResponseStatus(CONFLICT)
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}