package com.hongik.swagger;

import com.hongik.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResponseDto {

    private int code;

    private String status;

    private String message;

    public static ErrorResponseDto from(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode.getStatus().value(), errorCode.getStatus().name(), errorCode.getMessage());
    }
//    private ErrorResponseDto(ErrorCode errorCode) {
//        super(errorCode.toString(), errorCode.getMessage());
//    }
//
//    private ErrorResponseDto(ErrorCode errorCode, Exception e) {
//        super(errorCode.toString(), errorCode.getMessage());
//    }
//
//    private ErrorResponseDto(ErrorCode errorCode, String message) {
//        super(errorCode.toString(), errorCode.getMessage() + " - " + message);
//    }
//
//    public static ErrorResponseDto from(ErrorCode errorCode) {
//        return new ErrorResponseDto(errorCode);
//    }
//
//    public static ErrorResponseDto of(ErrorCode errorCode, Exception e) {
//        return new ErrorResponseDto(errorCode, e);
//    }
//
//    public static ErrorResponseDto of(ErrorCode errorCode, String message) {
//        return new ErrorResponseDto(errorCode, message);
//    }
}