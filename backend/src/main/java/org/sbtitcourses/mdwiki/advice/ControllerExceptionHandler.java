package org.sbtitcourses.mdwiki.advice;

import org.sbtitcourses.mdwiki.dto.error.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.RegistrationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RegistrationFailedException.class)
    private ResponseEntity<ErrorResponse> handleRegistrationFailedException(RegistrationFailedException e) {
        ErrorResponse response = new ErrorResponse("Ошибка при регистрации", new Date(), e.getErrors());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleException(ConstraintViolationException cve) {
        ErrorResponse response = new ErrorResponse(
                "Ошибка валидации",
                new Date(),
                List.of(cve.getMessage())
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<HttpStatus> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
