package com.cherryhouse.server.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.cherryhouse.server.user.User;

public class AuthRequest {

    public record LoginDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력입니다")
            String password
    ){
        public User toEntity(PasswordEncoder passwordEncoder){
            return User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
        }

        public static LoginDto toDto(User user){
            return new LoginDto(
                    user.getEmail(),
                    user.getPassword()
            );
        }

        public UsernamePasswordAuthenticationToken toAuthentication(){
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    public record JoinDto(

            @NotBlank(message = "닉네임은 필수 입력입니다")
            String username,

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력입니다")
            String password
    ){
        public User toEntity(PasswordEncoder passwordEncoder){
            return User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
        }
    }

    public record SendVerificationCodeDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email
    ){}

    public record EmailVerificationDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email,

            @NotBlank(message = "인증 코드는 필수 입력입니다")
            String code
    ){}
}