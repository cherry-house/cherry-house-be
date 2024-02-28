package com.cherryhouse.server._core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    // user --------------------

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    USER_EXISTS(HttpStatus.BAD_REQUEST, "이미 회원가입된 이메일입니다."),
    BAD_USER_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 회원 요청입니다."),
    BAD_USER_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호를 잘 못 입력하였습니다."),
    USER_LOGOUT(HttpStatus.BAD_REQUEST, "로그아웃 된 유저입니다."),
    // auth --------------------

    INVALID_TOKEN_EXCEPTION(HttpStatus.NOT_FOUND,"유효하지 않은 토큰입니다."),
    FAIL_TO_LOGOUT(HttpStatus.BAD_REQUEST,"로그아웃에 실패했습니다."),
    EMAIL_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 인증 코드 전송에 실패했습니다."),
    EMAIL_CODE_MATCH_FAILED(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 일치하지 않습니다."),

    // post --------------------

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),

    // chat --------------------

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방을 찾을 수 없습니다."),
    CHATROOM_EXISTS(HttpStatus.BAD_REQUEST, "이미 생성된 채팅방입니다."),

    // server ------------------

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 오류가 발생했습니다."),

    // etc ---------------------

    INVALID_REQUEST_DATA(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다.");

    private final HttpStatus status;
    private final String message;
}
