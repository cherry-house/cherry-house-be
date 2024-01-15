package com.cherryhouse.server._core.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private final ExceptionCode error;
    private final String message;

    public ApiException(ExceptionCode error, String message){
        this.error = error;
        this.message = message;
    }

    public ApiException(ExceptionCode error){
        this.error = error;
        this.message = error.getMessage();
    }
}
