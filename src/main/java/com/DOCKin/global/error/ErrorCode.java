package com.DOCKin.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버 내부 오류가 발생했습니다."),
    INVALID_TYPE_VALUE(400, "C004", "입력값의 타입이 적절하지 않습니다."),

    // Auth
    UNAUTHORIZED(401, "A001", "로그인이 필요한 서비스입니다."),
    ACCESS_DENIED(403, "A002", "해당 리소스에 대한 접근 권한이 없습니다."),
    TOKEN_EXPIRED(401, "A003", "인증 토큰이 만료되었습니다."),
    INVALID_TOKEN(401, "A004", "잘못된 인증 토큰입니다."),

    // User
    USER_NOT_FOUND(404, "U001", "존재하지 않는 사용자입니다."),
    USERID_DUPLICATION(400, "U002", "이미 가입된 사원번호입니다."),
    LOGIN_INPUT_INVALID(400, "U003", "사원번호 또는 비밀번호가 일치하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}