package com.cherryhouse.server._core.config;


import com.cherryhouse.server._core.security.JWTAccessDeniedHandler;
import com.cherryhouse.server._core.security.JWTAuthenticationEntryPoint;
import com.cherryhouse.server._core.security.JWTAuthenticationFilter;
import com.cherryhouse.server._core.security.TokenProvider;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
               //토큰 사용 방식 -> csrf disable
               .csrf(AbstractHttpConfigurer::disable);
       http.csrf(
               csrfCustomizer -> csrfCustomizer
                       .ignoringRequestMatchers(antMatcher("/h2-console/**"))
                       .disable()
       );

       http.headers(
               // h2-console에서 iframe을 사용함. X-Frame-Options을 위해 sameOrigin 설정
               headersCustomizer -> headersCustomizer
                       .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
       );

       //권한 설정
       http.authorizeHttpRequests(
               authorizeRequests -> authorizeRequests
                       .requestMatchers("/auth/**").permitAll()
                       .requestMatchers("/posts/**").permitAll()
                       .requestMatchers("/hearts/**").permitAll()
                       .requestMatchers("/users/**").permitAll()
                       .anyRequest().permitAll()
       );

//       http.exceptionHandling(
//               exceptionHandling -> exceptionHandling
//                       .accessDeniedHandler(jwtAccessDeniedHandler)
//                       .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//       );

        http.exceptionHandling(
                exceptionHandler -> exceptionHandler.accessDeniedHandler(
                        jwtAccessDeniedHandler::handle
                )
        );

        http.exceptionHandling(
                exceptionHandler -> exceptionHandler.authenticationEntryPoint(
                        jwtAuthenticationEntryPoint::commence
                )
        );

        http.sessionManagement(
                sessionCustomizer -> sessionCustomizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(new JWTAuthenticationFilter(tokenProvider,redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return http.build();

        //-------------------for version 6.0.2
//        httpSecurity.csrf().disable()
//
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//
//                // enable h2-console
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()
//
//                // 세션을 사용하지 않기 때문에 STATELESS로 설정
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                .and()
//                .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
//                .requestMatchers("/auth/**").permitAll() // 로그인 api
//                .requestMatchers("/posts/**").permitAll() // 회원가입 api
//                .requestMatchers(PathRequest.toH2Console()).permitAll()// h2-console, favicon.ico 요청 인증 무시
//                .requestMatchers("/favicon.ico").permitAll()
//                .anyRequest().authenticated() // 그 외 인증 없이 접근X
//
//                .and()
//                .addFilterBefore(new JWTAuthenticationFilter(tokenProvider),UsernamePasswordAuthenticationFilter.class); // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용
//
//        return httpSecurity.build();
    }

    //@Bean - 해당 메서드의 리턴되는 오브젝트트를 IoC로 등록해준다
    //비밀번호 암호화를 위한 메서드
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return new BCryptPasswordEncoder();
    }
}
