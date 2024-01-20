package com.cherryhouse.server._core.security;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.cherryhouse.server._core.security.UserPrincipal;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String USER_EMAIL = "email";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;            // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;


    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto.Response createToken(Authentication auth) {

        //권한 가져오기
        String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenExpire = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        UserPrincipal userPrincipal = (UserPrincipal)auth.getPrincipal();

        Claims claims = Jwts.claims();
        claims.put(AUTHORITIES_KEY,authorities);
        claims.put(USER_EMAIL,userPrincipal.getEmail());

        String accessToken = Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .setClaims(claims) // 정보 저장
                .signWith(key, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘과 , signature 에 들어갈 secret값 세팅
                .setExpiration(accessTokenExpire) // set Expire Time 해당 옵션 안넣으면 expire안함
                .compact();

        Date  refreshTokenExpire = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpire)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.Response.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpire.getTime())
                .refreshToken(refreshToken)
                .build();
    }


    //JWT 토큰을 복호화 하여 토큰에 들어있는 정보를 꺼냄
    public Authentication getAuthentication(String token, HttpServletRequest request) {

        //토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null)  {
            throw new ApiException(ExceptionCode.INVALID_TOKEN_EXCEPTION, "유효하지 않은 토큰입니다.");
        }


        //권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new UserPrincipal(
                claims.get(USER_EMAIL).toString(),
                "",
                authorities);



        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }



    //토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
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


}