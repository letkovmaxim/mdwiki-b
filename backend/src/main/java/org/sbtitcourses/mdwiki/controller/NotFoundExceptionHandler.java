package org.sbtitcourses.mdwiki.controller;

import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.DocumentNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleSpaceNotFoundException(SpaceNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Пространство не найдено",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePageNotFoundException(PageNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Страница не найден",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDocumentNotFoundException(DocumentNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Документ не найден",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
