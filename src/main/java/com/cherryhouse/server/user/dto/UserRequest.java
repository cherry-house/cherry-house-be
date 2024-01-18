package com.cherryhouse.server.user.dto;

import com.cherryhouse.server.user.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JoinDto{

        @NotNull
        private String username;

        @NotNull
        private String email;

        @NotNull
        private String password;


    }





}
