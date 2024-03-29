package com.cherryhouse.server._core.security;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String USER_EMAIL = "email";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;            // 1시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey){
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto.Response createToken(Authentication auth){

        //권한 가져오기
        String authorities = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenExpire = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        UserPrincipal userPrincipal = (UserPrincipal)auth.getPrincipal();

        Claims claims = Jwts.claims();
        claims.put(AUTHORITIES_KEY, authorities);
        claims.put(USER_EMAIL, userPrincipal.getEmail());

        String accessToken = Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setClaims(claims) // 정보 저장
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 , signature 에 들어갈 secret 값 세팅
                .setExpiration(accessTokenExpire) // set Expire Time 해당 옵션 안넣으면 expire 안함
                .compact();

        Date  refreshTokenExpire = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpire)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.Response.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpire.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    //JWT 토큰을 복호화 하여 토큰에 들어있는 정보를 꺼냄
    public Authentication getAuthentication(String token){

        //토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null)  {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION, "유효하지 않은 토큰입니다.");
        }

        //권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new UserPrincipal(
                claims.get(USER_EMAIL).toString(),
                "",
                authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            if (e instanceof SecurityException) {
                log.info("잘못된 JWT 서명입니다.");
            } else if (e instanceof ExpiredJwtException) {
                log.info("만료된 JWT 토큰입니다.");
            } else if (e instanceof UnsupportedJwtException) {
                log.info("지원되지 않는 JWT 토큰입니다.");
            } else if (e instanceof MalformedJwtException) {
                log.info("JWT 토큰이 잘못되었습니다.");
            } else {
                log.info("알 수 없는 JWT 예외가 발생했습니다.");
            }
            throw e;
        }
    }

    private Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public long getAccessTokenExpireTime(){
        return ACCESS_TOKEN_EXPIRE_TIME;
    }

    public Long getExpiration(String accessToken){
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        long now = new Date().getTime();

        return (expiration.getTime() - now);
    }

    public String getEmail(String accessToken){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .get(USER_EMAIL, String.class);
    }
}