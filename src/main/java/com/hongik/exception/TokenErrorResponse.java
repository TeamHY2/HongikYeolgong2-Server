package com.hongik.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenErrorResponse {

    private String message;

    @Builder
    public TokenErrorResponse(final String message) {
        this.message = message;
    }

    public static TokenErrorResponse of(final String message) {
        return TokenErrorResponse.builder()
                .message(message)
                .build();
    }
}
