package com.DOCKin.global.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        System.out.println("보안 에러 발생 경로: " + request.getRequestURI());
        System.out.println("에러 메시지: " + authException.getMessage());

        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized: 인증 정보가 없습니다."
        );
    }
}