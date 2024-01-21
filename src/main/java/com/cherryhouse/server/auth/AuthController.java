package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //TODO : validation + errors
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody AuthDto.JoinDto joinDto) {
        authService.join(joinDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginDto loginDto) {
        authService.login(loginDto);
        return ResponseEntity.ok().body(ApiResponse.success(authService.login(loginDto)));
    }
}
