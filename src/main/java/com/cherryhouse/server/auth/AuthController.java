package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.auth.dto.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import static com.cherryhouse.server._core.security.TokenProvider.REFRESH_TOKEN_EXPIRE_TIME;

@Tag(name = "AUTH", description = "인증 API 입니다.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 합니다.")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid AuthRequest.JoinDto request, Errors errors) {
        authService.join(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Operation(summary = "로그인", description = "사용자가 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest.LoginDto request, Errors errors) {
        TokenDto.Response response = authService.login(request);
        HttpCookie httpCookie = ResponseCookie.from("refresh-token", response.getRefreshToken())
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccessToken())
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "토큰 재발급", description = "사용자의 토큰을 재발급합니다.")
    @PostMapping("/token")
    public ResponseEntity<?> token(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                   @RequestHeader("Authorization") String requestAccessToken){
        TokenDto.Response response = authService.reissue(requestAccessToken, requestRefreshToken);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @Operation(summary = "로그아웃", description = "사용자가 로그아웃합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        authService.logout(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.success());
    }

    @Operation(summary = "이메일 인증 코드 발송", description = "사용자의 이메일로 인증 코드를 발송합니다.")
    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody @Valid AuthRequest.SendVerificationCodeDto request, Errors errors) {
        authService.sendVerificationCode(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "사용자의 이메일 인증 코드를 확인합니다.")
    @PostMapping("/confirm-verification-code")
    public ResponseEntity<?> confirmVerificationCode(@RequestBody @Valid AuthRequest.ConfirmVerificationCodeDto request, Errors errors) {
        authService.confirmVerificationCode(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
