package com.hongik.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenErrorResponse {

    private int code;
    private String status;
    private String message;

    @Builder
    public TokenErrorResponse(final int code, final String status, final String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public static TokenErrorResponse of(final int code, final String status, final String message) {
        return TokenErrorResponse.builder()
                .code(code)
                .status(status)
                .message(message)
                .build();
    }
}
