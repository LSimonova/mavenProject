package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(NotFoundException exception) {
        return new ResponseEntity<>(new MessageException(exception.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }

}
