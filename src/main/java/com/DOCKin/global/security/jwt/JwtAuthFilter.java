package com.DOCKin.global.security.jwt;

import com.DOCKin.member.service.CustomUserDetailsService;
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
        String path = request.getRequestURI();
        if (path.startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        // [로그 1] 헤더가 들어오는지 확인
        log.info("Request URI: {}, Authorization Header: {}", path, authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (jwtUtil.isValidToken(token) && !jwtBlacklist.isBlacklisted(token)) {
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
                            // [로그 2] 인증 성공 확인
                            log.info("인증 성공: user_id = {}", userId);
                        }
                    } else {
                        log.warn("토큰에서 userId 추출 실패");
                    }
                } else {
                    log.warn("유효하지 않은 토큰이거나 블랙리스트에 등록된 토큰입니다.");
                }
            } catch (Exception e) {
                log.error("JWT 인증 에러: {}", e.getMessage());
            }
        } else {
            // [로그 3] 토큰이 없는 경우
            log.warn("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
        }

        filterChain.doFilter(request, response);
    }
}