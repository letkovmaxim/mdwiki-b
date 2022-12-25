package org.sbtitcourses.mdwiki.advice;

import org.sbtitcourses.mdwiki.dto.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ErrorResponse> handleException(ConstraintViolationException cve) {
        ErrorResponse response = new ErrorResponse(
                "Ошибка валидации",
                new Date(),
                List.of(cve.getMessage())
        );

        return ResponseEntity.badRequest().body(response);
    }
}
