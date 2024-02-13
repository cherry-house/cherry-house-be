package com.cherryhouse.server.user.dto;

import jakarta.validation.constraints.*;

public class UserRequest {

    public record UpdateInfoDto(

            @Email
            String email,
            @NotBlank(message = "닉네임은 필수 입력입니다.")
            @Size(min = 2, max = 15, message = "닉네임은 2글자 이상 15글자 이하로 입력하세요.")
            @NotNull(message = "닉네임은 필수 입력입니다.")
            String username,
            String introduction
    ){}

    public record UpdateImgDto(
            String email,
            String img
    ){}

    public record UpdatePwdDto(
            String email,
            @NotBlank(message = "비밀번호는 필수 입력입니다.")
            @NotNull(message = "비밀번호는 필수 입력입니다.")
            @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 영문자와 숫자를 포함하여 8글자 이상이여야 합니다.")
            String password
    ){}
}
