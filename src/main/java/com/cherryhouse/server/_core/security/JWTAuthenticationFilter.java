package com.cherryhouse.server._core.security;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends GenericFilter {
    private static final Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = resolveToken((HttpServletRequest)request);

        if( token != null && tokenProvider.validateToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            log.debug("Authentication 정보 : {}", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else{
            log.debug("유효한 JWT 토큰이 없습니다.");
        }

        chain.doFilter(request,response);
    }


    //request header에서 토큰 정보를 추출함
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
