package com.ecocursos.ecocursos.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerException {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ApiError> handlerObjectNotFoundException(ObjectNotFoundException e,
                                                                   HttpServletRequest http) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage(), http.getServletPath(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ApiError> handlerErrorException(ErrorException errorException, HttpServletRequest http) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST.value(), errorException.getMessage(), http.getServletPath(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
