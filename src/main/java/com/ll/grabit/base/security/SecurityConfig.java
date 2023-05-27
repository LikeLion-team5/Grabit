package com.ll.grabit.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                authorize -> authorize
                        .anyRequest().permitAll()
        ).csrf(
                /*
                식당 등록 테스트를 위해서 csrf 토큰 꺼둔다. POST, PUT, DELETE 같은
                데이터 변경이 일어날만한 HTTP 메서드에서는 csrf 토큰을 함께 받아야지 실행이 된다.
                따라서 원할한 테스트를 위해서 꺼둔다.
                 */
                csrfConfigurer -> csrfConfigurer
                        .disable()
        ).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
