package com.cherryhouse.server.user.dto;

import com.cherryhouse.server.user.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter
    @Setter
    public static class JoinDTO{

        @NotEmpty
        private String email ;

        @NotEmpty
        private String password;

        @NotEmpty
        private String username;


        public User toEntity() {
            return User.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .build();
        }
    }



}
