package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid AuthRequest.JoinDto request, Errors errors) {
        authService.join(request);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest.LoginDto request, Errors errors) {
        authService.login(request);
        return ResponseEntity.ok().body(ApiResponse.success(authService.login(request)));
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
