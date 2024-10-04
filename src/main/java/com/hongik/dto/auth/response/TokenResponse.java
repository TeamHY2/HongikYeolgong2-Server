package com.hongik.dto.auth.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenResponse {

    private String accessToken;

    @Builder
    public TokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse of(final String accessToken) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
