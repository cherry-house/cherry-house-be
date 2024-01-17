package com.cherryhouse.server._core.config;


import com.cherryhouse.server._core.security.JWTAccessDeniedHandler;
import com.cherryhouse.server._core.security.JWTAuthenticationEntryPoint;
import com.cherryhouse.server._core.security.JWTAuthenticationFilter;
import com.cherryhouse.server._core.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider TokenProvider;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
               //토큰 사용 방식 -> csrf disable
               .csrf(AbstractHttpConfigurer::disable)
               .exceptionHandling(
                       exceptionHandling -> exceptionHandling
                               .accessDeniedHandler(jwtAccessDeniedHandler)
                               .authenticationEntryPoint(jwtAuthenticationEntryPoint));


       http.headers(
               // h2-console에서 iframe을 사용함. X-Frame-Options을 위해 sameOrigin 설정
               headersCustomizer -> headersCustomizer
                       .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
       );

       http.authorizeHttpRequests(
               authorizeRequests -> authorizeRequests
                       .requestMatchers("/user/**").permitAll()
                       .requestMatchers("/posts/**").permitAll()
                       .anyRequest().permitAll()
       );

       http.exceptionHandling(
               exceptionHandling -> exceptionHandling
                       .accessDeniedHandler(jwtAccessDeniedHandler)
                       .authenticationEntryPoint(jwtAuthenticationEntryPoint)
       );

        http.sessionManagement(
                sessionCustomizer -> sessionCustomizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return new BCryptPasswordEncoder();
    }

}
