package com.cherryhouse.server._core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),

    INVALID_REQUEST_DATA(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 오류가 발생했습니다."),



    INVALID_TOKEN_EXCEPTION(HttpStatus.NOT_FOUND,"유효하지 않은 토큰입니다.");


    private final HttpStatus status;
    private final String message;
}
