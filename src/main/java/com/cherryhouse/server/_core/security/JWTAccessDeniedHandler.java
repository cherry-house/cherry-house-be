package com.cherryhouse.server._core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("권한이 없습니다.: {}", accessDeniedException.getMessage());
        // 필요한 권한이 없이 접근하려 할때 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "해당 리소스에 접글할 권한이 없습니다.");
    }
}
