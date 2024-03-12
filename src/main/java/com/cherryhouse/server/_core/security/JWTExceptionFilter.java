package com.cherryhouse.server._core.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JWTExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("*** JWT Exception Filtering... ***");
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("*** 만료된 토큰 ***");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (MalformedJwtException e) {
            log.info("*** 잘못된 토큰 ***");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("*** 지원되지 않는 토큰 ***");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (JwtException e) {
            log.info("*** JWT 예외 발생 ***");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT 예외 발생 : " + e.getMessage());
        } catch (RuntimeException e) {
            log.info("*** 내부 서버 에러. ***");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "런타임 예외 발생 : " + e.getMessage());
        }
    }
}
