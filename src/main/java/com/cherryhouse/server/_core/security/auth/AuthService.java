package com.cherryhouse.server._core.security.auth;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.security.dto.AuthDto;
import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import com.cherryhouse.server.user.UserService;
import com.cherryhouse.server.user.dto.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public User join(UserRequest.JoinDto userRequest) {
        if( userRepository.existsByEmail(userRequest.getEmail())) {
            throw new ApiException(ExceptionCode.USER_EXISTS, "이미 회원가입된 이메일입니다.");
        }

        User user = User.builder()
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .email(userRequest.getEmail())
                .build();
        UserPrincipal.create(user);

        return userRepository.save(user);

    }

    @Transactional
    public TokenDto.Response login(AuthDto.LoginRequestDto loginRequestDto){

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 2. 실제 검증 ( 비밀번호 체크 )
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto.Response tokenResponseDto = tokenProvider.createToken(authentication);

        return tokenResponseDto;


    }

    @Transactional
    public void logout(TokenDto.Request tokenRequest, HttpServletRequest request){
        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken(),request);
    }


}
