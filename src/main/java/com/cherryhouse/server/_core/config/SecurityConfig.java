package com.cherryhouse.server._core.config;


import com.cherryhouse.server._core.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

//스프링 시큐리티 필터가 스프링 필터체인에 등록 된다.
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;
    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RedisTemplate<String,String> redisTemplate;
    private final JWTExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
               //토큰 사용 방식 -> csrf disable
            .csrf(AbstractHttpConfigurer::disable)
                .csrf(
                        csrfCustomizer -> csrfCustomizer
                                .ignoringRequestMatchers(antMatcher("/h2-console/**"))
                                .disable()
                )
                .headers(
                        headersCustomizer -> headersCustomizer
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/posts/**").permitAll()
                                .requestMatchers("/hearts/**").permitAll()
                                .requestMatchers("/users/**").permitAll()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(
                        exceptionHandler -> exceptionHandler
                                .accessDeniedHandler(jwtAccessDeniedHandler::handle)
                )
                .exceptionHandling(
                        exceptionHandler -> exceptionHandler
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint::commence)
                )
                .sessionManagement(
                        sessionCustomizer -> sessionCustomizer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JWTAuthenticationFilter(tokenProvider,redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JWTAuthenticationFilter.class);

        return http.build();
    }

    //@Bean - 해당 메서드의 리턴되는 오브젝트트를 IoC로 등록해준다
    //비밀번호 암호화를 위한 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return new BCryptPasswordEncoder();
    }
}
