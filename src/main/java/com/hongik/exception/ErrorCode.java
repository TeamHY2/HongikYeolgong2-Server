package com.hongik.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    OK(HttpStatus.OK, "응답 성공");

    private final HttpStatus status;

    private final String message;
}
