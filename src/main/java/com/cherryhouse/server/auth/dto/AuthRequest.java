package com.cherryhouse.server.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequest {

    public record LoginDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력입니다")
            String password
    ){
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
    ){}

    public record SendVerificationCodeDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email
    ){}

    public record ConfirmVerificationCodeDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다")
            String email,

            @NotBlank(message = "인증 코드는 필수 입력입니다")
            String code
    ){}
}