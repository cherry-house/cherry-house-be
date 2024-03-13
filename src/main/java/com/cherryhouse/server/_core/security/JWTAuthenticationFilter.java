package com.cherryhouse.server._core.security;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;



    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        log.info("*** Verifying token... ***");

        String token = resolveToken(request);
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 블랙리스트 확인
            String isLogout = redisTemplate.opsForValue().get(token);
            if (ObjectUtils.isEmpty(isLogout)){
                log.info("*** 블랙리스트에 없음 ***");
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.info("*** 블랙리스트에 있음 ***");
                throw new RuntimeException("블랙리스트 유저");
            }
        }
        chain.doFilter(request, response);
    }

    //request header에서 토큰 정보를 추출함
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
