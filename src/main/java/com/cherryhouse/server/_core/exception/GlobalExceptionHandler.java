package com.cherryhouse.server._core.exception;

import com.cherryhouse.server._core.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> error(ApiException ex){
        return ResponseEntity.status(ex.getError().getStatus()).body(ApiResponse.error(ex.getError(), ex.getMessage()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> serverError(Exception ex){
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        return ResponseEntity.status(status).body(ApiResponse.error(status, ex.getMessage()));
//    }
}
