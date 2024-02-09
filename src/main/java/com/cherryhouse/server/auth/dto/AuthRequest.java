package com.cherryhouse.server.auth.dto;

import jakarta.validation.constraints.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequest {

    public record LoginDto(

            @Email
            @NotBlank(message = "이메일은 필수 입력입니다.")
            @NotNull(message = "이메일은 필수 입력입니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력입니다.")
            @NotNull(message = "비밀번호는 필수 입력입니다.")
            String password
    ){
        public UsernamePasswordAuthenticationToken toAuthentication(){
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    public record JoinDto(

            @NotBlank(message = "닉네임은 필수 입력입니다.")
            @Size(min = 2, max = 15, message = "닉네임은 2글자 이상 15글자 이하로 입력하세요.")
            String username,

            @Email(message = "올바르지 않은 이메일 형식입니다.")
            @NotBlank(message = "이메일은 필수 입력입니다.")
            String email,

            @NotBlank(message = "비밀번호는 필수 입력입니다")
            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 영문자와 숫자를 포함하여 8글자 이상이여야 합니다.")
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