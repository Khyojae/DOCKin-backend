package com.DOCKin.global.security.jwt;

import com.DOCKin.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtBlacklist jwtBlacklist;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.isValidToken(token) && !jwtBlacklist.isBlacklisted(token)) {
                    // JwtUtil에서 String으로 반환하는 getUserId 호출
                    String userId = jwtUtil.getUserId(token);

                    if (userId != null) {
                        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);

                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    } else {
                        log.warn("토큰에서 userId를 추출하는 데 실패했습니다.");
                    }
                }
            } catch (Exception e) {
                log.error("JWT 인증 에러: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}