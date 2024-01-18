package com.cherryhouse.server._core.security.auth;

import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server._core.security.dto.AuthDto;
import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.user.UserRepository;
import com.cherryhouse.server.user.UserService;
import com.cherryhouse.server.user.dto.UserRequest;
import com.cherryhouse.server.user.dto.UserRequest.*;
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


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDto joinDto) {
        authService.join(joinDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDto.LoginRequestDto loginDto) {
        authService.login(loginDto);
        return ResponseEntity.ok().body(ApiResponse.success(authService.login(loginDto)));
    }








}
