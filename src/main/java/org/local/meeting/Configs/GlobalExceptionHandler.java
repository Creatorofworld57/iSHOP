package org.local.meeting.Configs;

import org.local.meeting.Exeptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpired(TokenExpiredException ex) {
        ErrorResponse error = new ErrorResponseException(
               HttpStatus.UNAUTHORIZED
        );//изменить конструктор, заполнить большим количеством данных
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}