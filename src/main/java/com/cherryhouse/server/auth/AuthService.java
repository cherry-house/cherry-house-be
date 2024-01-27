package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

import static com.cherryhouse.server._core.security.TokenProvider.REFRESH_TOKEN_EXPIRE_TIME;
import static com.cherryhouse.server.auth.CodeGenerator.generateCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void join(AuthRequest.JoinDto joinDto){
        if(userRepository.existsByEmail(joinDto.email())) {
            throw new ApiException(ExceptionCode.USER_EXISTS, "이미 회원가입된 이메일입니다.");
        }

        User user = joinDto.toEntity(passwordEncoder);
        UserPrincipal.create(user);

        userRepository.save(user);
    }

    @Transactional
    public TokenDto.Response login(AuthRequest.LoginDto loginDto){

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        // 2. 실제 검증 ( 비밀번호 체크 )
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto.Response tokenResponseDto = tokenProvider.createToken(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName() + ":refresh_token",
                tokenResponseDto.getRefreshToken(),
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );

        return tokenResponseDto;
    }

    @Transactional
    public void logout(String requestAccessToken){
        //토큰 검증
        if (!tokenProvider.validateToken(requestAccessToken)){
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION);
        }

        //access token에서 user email 가져오기
        Authentication authentication = tokenProvider.getAuthentication(requestAccessToken);

        //redis에 해당 user email로 저장된 refresh token 있는지 확인, 있을 경우 삭제
        if (redisTemplate.opsForValue().get(authentication.getName() + ":refresh_token") != null){
            redisTemplate.delete(authentication.getName());
        }else{
            throw new ApiException(ExceptionCode.FAIL_TO_LOGOUT);
        }

        Long expiration = tokenProvider.getExpiration(requestAccessToken);
        redisTemplate.opsForValue().set(
                requestAccessToken,
                "logout",
                expiration,
                TimeUnit.MILLISECONDS
        );
    }

    @Transactional
    public TokenDto.Response reissue(String requestAccessToken, String requestRefreshToken){

        if (!tokenProvider.validateToken(requestAccessToken)){
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION);
        }

        Authentication authentication = tokenProvider.getAuthentication(requestAccessToken);
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName() + ":refresh_token");

        //로그아웃 된 경우
        if (refreshToken == null){
            throw new  RuntimeException("로그아웃 된 사용자입니다.");
        }

        //refresh token 검증
        if(!refreshToken.equals(requestRefreshToken)) {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION);
        }
        TokenDto.Response tokenResponseDto = tokenProvider.createToken(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName() + ":refresh_token",
                tokenResponseDto.getRefreshToken(),
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );

        return tokenResponseDto;
    }

    @Transactional
    public void sendVerificationCode(AuthRequest.SendVerificationCodeDto sendDto){
        //TODO: 중복 확인

        String code = generateCode();

        redisTemplate.opsForValue().set(
                sendDto.email() + ":verification_code",
                code,
                3,
                TimeUnit.MINUTES
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendDto.email());
        message.setSubject("메일 인증 번호입니다.");
        message.setText("인증번호: " + code);
        javaMailSender.send(message);
    }

    @Transactional
    public void confirmVerificationCode(AuthRequest.ConfirmVerificationCodeDto confirmDto){
        String code = redisTemplate.opsForValue().get(confirmDto.email() + ":verification_code");

        if(!confirmDto.code().equals(code)){
            throw new ApiException(ExceptionCode.EMAIL_CODE_MATCH_FAILED);
        }

        redisTemplate.delete(confirmDto.email() + ":verification_code");
    }
}