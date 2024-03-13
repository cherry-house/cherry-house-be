package com.cherryhouse.server._core.util;

import com.cherryhouse.server._core.exception.ExceptionCode;
import io.swagger.v3.oas.annotations.media.Schema;
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

    public static <T>ResponseDto<T> error(ExceptionCode error, String message){
        return new ResponseDto<>(error.getStatus().value(), null, message);
    }

    public static <T>ResponseDto<T> error(HttpStatus status, String message){
        return new ResponseDto<>(status.value(), null, message);
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseDto<T>{

        @Schema(description = "상태 코드", nullable = false, example = "200")
        private int status;

        @Schema(description = "데이터", nullable = false)
        private T response;

        @Schema(description = "에러 메시지", nullable = false, example = "null")
        private String errorMessage;
    }
}
