package com.DOCKin.global.security.config;

import com.DOCKin.global.security.jwt.JwtAuthFilter;
import com.DOCKin.global.security.jwt.JwtBlacklist;
import com.DOCKin.global.security.jwt.JwtUtil;
import com.DOCKin.member.service.CustomUserDetailsService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/",
            "/error",
            "/favicon.ico",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/member/login",
            "/member/signup",
            "/api/safety/**",
            "/api/work-logs/**",
            "/api/attendance/**",
            "/ws/**",
            "/ws-stomp/**"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 경로를 배열로 명시적으로 선언하여 모호성을 제거합니다.
        return (web) -> web.ignoring()
                .requestMatchers(new String[]{"/favicon.ico", "/css/**", "/js/**", "/img/**"});
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtBlacklist jwtBlacklist) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                // 1. AI 관련 경로를 최상단에 배치하여 우선순위를 높입니다.
                .requestMatchers("/api/ai/**").authenticated()
                // 2. 화이트리스트 (배열을 그대로 전달)
                .requestMatchers(AUTH_WHITELIST).permitAll()
                // 3. 그 외 모든 요청 인증 필요
                .anyRequest().authenticated());

        http.securityContext(context -> context
                .requireExplicitSave(false)
        );

        // JWT 필터 추가
        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil, jwtBlacklist),
                UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}