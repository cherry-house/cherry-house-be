package com.cherryhouse.server._core.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Slf4j
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

//
//    private final HandlerExceptionResolver resolver;
//
//    public JWTAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
//        this.resolver = resolver;
//    }
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        log.warn("자격 증명이 필요합니다 - {}", authException.getMessage());
//
//        //log.info("여기2.. :{}",request.getAttribute("exception"));
//        log.info("여기2.. :{}",request.getHeader("exception"));
//        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//        resolver.resolveException(request, response, null, (Exception) request.getAttribute("exception"));
//        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("자격 증명이 필요합니다 - {}", authException.getMessage());
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
