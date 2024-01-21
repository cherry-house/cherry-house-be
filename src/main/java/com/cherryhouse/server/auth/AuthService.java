package com.cherryhouse.server.auth;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.TokenProvider;
import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.security.dto.TokenDto;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JavaMailSender javaMailSender;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789!@#$%";
    private static final int CODE_LENGTH = 8;

    @Transactional
    public void join(AuthRequest.JoinDto joinDto) {
        if( userRepository.existsByEmail(joinDto.email())) {
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

        return tokenResponseDto;
    }

    @Transactional
    public void logout(TokenDto.Request tokenRequest, HttpServletRequest request){
        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken(),request);
    }

    @Transactional
    public void sendVerificationCode(AuthRequest.SendVerificationCodeDto sendDto){
        String code = createCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendDto.email());
        message.setSubject("메일 인증 번호입니다.");
        message.setText("인증번호: " + code);

        javaMailSender.send(message);
    }

    @Transactional
    public void confirmVerificationCode(AuthRequest.ConfirmVerificationCodeDto confirmDto){

    }

    private String createCode(){
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            StringBuilder code = new StringBuilder();

            for(int i = 0; i <= CODE_LENGTH; i++){
                int index = secureRandom.nextInt(CHARACTERS.length());
                code.append(CHARACTERS.charAt(index));
            }

            return code.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(ExceptionCode.EMAIL_CODE_CREATION_FAILED);
        }
    }
}
