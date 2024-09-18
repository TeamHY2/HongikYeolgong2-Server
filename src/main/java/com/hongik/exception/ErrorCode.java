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
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");

    private final HttpStatus status;

    private final String message;
}
