package com.cherryhouse.server._core.util;

import lombok.AllArgsConstructor;

public class ApiResponse {

    public static <T>ResponseDto<T> success(){
        return new ResponseDto<>(200, null, null);
    }

    public static <T>ResponseDto<T> success(T response){
        return new ResponseDto<>(200, response, null);
    }

    @AllArgsConstructor
    public static class ResponseDto<T>{
        private int status;
        private T response;
        private String errorMessage;
    }
}
