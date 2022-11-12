package org.sbtitcourses.mdwiki.util;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Глабальный обработчик исключений контроллеров
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Метод, отвечающий за обработку исключений валидации
     * @param e объект исключения
     * @return DTO сообщения об ошибке с кодом 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse response = new ErrorResponse(errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
