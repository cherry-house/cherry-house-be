package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.cherryhouse.server.auth.CodeGenerator.generateCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate<String,String> redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    @Transactional
    public void join(AuthRequest.JoinDto joinDto) {
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
                authentication.getName(),
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
        log.info("authentication Name : {}",authentication.getName());

        log.info("Before redisTemplate RT : {}",redisTemplate.opsForValue().get(authentication.getName()));
        if (redisTemplate.opsForValue().get(authentication.getName()) != null){
            redisTemplate.delete(authentication.getName());
            log.info("삭제 성공");

        }else{
            log.info("삭제 실패");
            throw new ApiException(ExceptionCode.FAIL_TO_LOGOUT);
        }
        log.info("After redisTemplate RT : {}",redisTemplate.opsForValue().get(authentication.getName()));

        Long expiration = tokenProvider.getExpiration(requestAccessToken);
        redisTemplate.opsForValue()
                .set(requestAccessToken,"logout",expiration,TimeUnit.MILLISECONDS);

    }



    @Transactional
    public TokenDto.Response reissue(String requestAccessToken, String requestRefreshToken){

        if ( !tokenProvider.validateToken(requestAccessToken)){
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION);
        }
        //access token에서 user email 가져오기
        Authentication authentication = tokenProvider.getAuthentication(requestAccessToken);

        //redis에서 refresh token 가져오기
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());

        //로그아웃 된 경우
        if (redisTemplate.opsForValue().get(authentication.getName()) == null){
            throw new  RuntimeException("로그아웃 된 사용자입니다.");
        }

        //refresh token 검증
        if(!refreshToken.equals(requestRefreshToken)) {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION);
        }
        TokenDto.Response tokenResponseDto = tokenProvider.createToken(authentication);

        redisTemplate.opsForValue().set(
                authentication.getName(),
                tokenResponseDto.getRefreshToken(),
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.SECONDS
        );

        return tokenResponseDto;


    }

    @Transactional
    public void sendVerificationCode(AuthRequest.SendVerificationCodeDto sendDto){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendDto.email());
        message.setSubject("메일 인증 번호입니다.");
        message.setText("인증번호: " + generateCode());
        javaMailSender.send(message);
    }

    @Transactional
    public void confirmVerificationCode(AuthRequest.ConfirmVerificationCodeDto confirmDto){
        String code = "임시";
        if(!confirmDto.code().equals(code)){
            throw new ApiException(ExceptionCode.EMAIL_CODE_MATCH_FAILED);
        }
    }
}
