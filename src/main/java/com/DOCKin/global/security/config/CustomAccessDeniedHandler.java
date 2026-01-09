package com.DOCKin.global.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import java.io.IOException;
import java.time.LocalDateTime;
import com.DOCKin.dto.ErrorResponseDto;
//로그인은 했는데 권한이 없을때
@Slf4j(topic ="Forbidden_EXCEPTION_HANDLER")
@AllArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("NO Authorities", accessDeniedException);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied: 권한이 부족합니다.",
                LocalDateTime.now());

        String responseBody = objectMapper.writeValueAsString(errorResponseDto);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
