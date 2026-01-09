package com.DOCKin.global.config;

import com.DOCKin.global.error.ErrorCode;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {
    @Bean
    @Primary
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DOCKin API 명세서")
                        .description(generateErrorCodeTable()) // 에러 코드 표 주입
                        .version("1.0.0"));
    }

    private String generateErrorCodeTable() {
        return "## 공통 에러 코드\n" +
                "| Status | Code | Message |\n" +
                "| :--- | :--- | :--- |\n" +
                Arrays.stream(ErrorCode.values())
                        .map(error -> String.format("| %d | %s | %s |",
                                error.getStatus(),
                                error.getCode(),
                                error.getMessage()))
                        .collect(Collectors.joining("\n"));
    }
}
