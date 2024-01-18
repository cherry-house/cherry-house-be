package com.cherryhouse.server.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.cherryhouse.server.user.User;

public class AuthDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginDto {

        @NotNull
        private String email;
        @NotNull
        private String password;

        public User toEntity(PasswordEncoder passwordEncoder) {
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
        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);

        }
    }

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

        public User toEntity(PasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
        }
    }

}