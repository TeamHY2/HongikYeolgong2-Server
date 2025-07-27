package com.hongik.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    OK(HttpStatus.OK, "응답 성공"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생하였습니다 관리자에게 문의해주세요."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다"),
    NOT_AUTHORITY(HttpStatus.FORBIDDEN, "권한이 존재하지 않습니다."),
    REGISTRATION_INCOMPLETE(HttpStatus.FORBIDDEN, "회원가입이 완료되지 않았습니다."),

    // User
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_DUPLICATE_USER(HttpStatus.NOT_FOUND, "중복된 회원이 존재하지 않습니다."),

    // JWT
    INVALID_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_EXPIRATION_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "기간이 만료된 토큰입니다."),

    // WeekField
    ALREADY_EXIST_WEEK(HttpStatus.CONFLICT, "이미 존재하는 연도에 주차데이터입니다."),

    // StudySession
    NOT_FOUND_STUDY_SESSION(HttpStatus.NOT_FOUND, "존재하지 않는 스터디 세션입니다.");

    private final HttpStatus status;

    private final String message;
}
