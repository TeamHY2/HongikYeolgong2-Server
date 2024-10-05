package com.hongik.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    OK(HttpStatus.OK, "응답 성공"),

    // User
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),

    // JWT
    INVALID_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_EXPIRATION_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "기간이 만료된 토큰입니다.");

    private final HttpStatus status;

    private final String message;
}
