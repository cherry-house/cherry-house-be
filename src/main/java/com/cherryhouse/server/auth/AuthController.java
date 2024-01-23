package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server._core.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid AuthRequest.JoinDto request, Errors errors) {
        authService.join(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest.LoginDto request, Errors errors) {
        TokenDto.Response response = authService.login(request);
        HttpCookie httpCookie = ResponseCookie.from("refresh-token", response.getRefreshToken())
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
                .httpOnly(true)
                .secure(true)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, httpCookie.toString()).header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccessToken()).body(ApiResponse.success(response));
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                   @RequestHeader("Authorization") String requestAccessToken){

        return ResponseEntity.ok().body(ApiResponse.success(authService.reissue(requestAccessToken,requestRefreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        authService.logout(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ApiResponse.success());
    }



    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody @Valid AuthRequest.SendVerificationCodeDto request, Errors errors) {
        authService.sendVerificationCode(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PostMapping("/confirm-verification-code")
    public ResponseEntity<?> confirmVerificationCode(@RequestBody @Valid AuthRequest.ConfirmVerificationCodeDto request, Errors errors) {
        authService.confirmVerificationCode(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
