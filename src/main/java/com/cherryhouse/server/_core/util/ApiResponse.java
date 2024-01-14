package com.cherryhouse.server._core.util;

import com.cherryhouse.server._core.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse {

    public static <T>ResponseDto<T> success(){
        return new ResponseDto<>(200, null, null);
    }

    public static <T>ResponseDto<T> success(T response){
        return new ResponseDto<>(200, response, null);
    }

    public static <T>ResponseDto<T> error(ExceptionCode error){
        return new ResponseDto<>(error.getStatus().value(), null, error.getMessage());
    }

    public static <T>ResponseDto<T> error(HttpStatus status, String message){
        return new ResponseDto<>(status.value(), null, message);
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseDto<T>{
        private int status;
        private T response;
        private String errorMessage;
    }
}
