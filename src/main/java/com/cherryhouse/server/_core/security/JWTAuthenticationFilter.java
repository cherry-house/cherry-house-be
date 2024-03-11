package com.cherryhouse.server._core.security;

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

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            String isLogout = redisTemplate.opsForValue().get(token);
            if (ObjectUtils.isEmpty(isLogout)){
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Authentication 정보 : {}", authentication);
            }
        } else {
            log.debug("유효한 JWT 토큰이 없습니다.");
        }
        chain.doFilter(request, response);
    }

//@Override
//public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//    log.info("111");
//    try{
//        log.info("222");
//        String token = resolveToken(request);
//            String isLogout = redisTemplate.opsForValue().get(token);
//            if (ObjectUtils.isEmpty(isLogout)){
//                Authentication authentication = tokenProvider.getAuthentication(token);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                log.debug("Authentication 정보 : {}", authentication);
//            }
//    } catch (BadCredentialsException e) {
//        // 비밀번호가 잘못 입력되었을 때 처리할 로직
//        log.info("비밀번호가 잘못 입력되었습니다.");
//        request.setAttribute("exception", e);
//    }catch (Exception e){
//        log.info("444");
//        log.info("여기1.. :{}",e.getMessage());
//        request.setAttribute("exception",e);
//    }
//    log.info("555");
//    //log.info("666 : {}", request.getAttribute("exception"));
//    chain.doFilter(request, response);
//}

    //request header에서 토큰 정보를 추출함
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
