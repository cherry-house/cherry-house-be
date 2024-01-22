package com.cherryhouse.server.auth.refreshToken;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Getter
@RedisHash(value = "refreshToken", timeToLive = 60)
public class RefreshToken {

    @Id
    private Long id;

    private String refreshToken;

    private String userEmail;

    public RefreshToken(final String refreshToken, final String userEmail){
        this.refreshToken = refreshToken;
        this.userEmail = userEmail;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public String getUserEmail(){
        return userEmail;
    }



}
