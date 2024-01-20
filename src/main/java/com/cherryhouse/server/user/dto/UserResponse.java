package com.cherryhouse.server.user.dto;

import com.cherryhouse.server.user.User;
import lombok.*;

public class UserResponse {

    //TODO : record 로 변경하기
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserResponseDto {
        private String email;
        private String username;
        private String profileImageUrl;

        public static UserResponse.UserResponseDto toDto(User user){
            return new UserResponse.UserResponseDto(
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImage()

            );
        }
    }
}
