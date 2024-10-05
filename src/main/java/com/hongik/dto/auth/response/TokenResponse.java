package com.hongik.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenResponse {

    @Schema(example = "eyJaiskdfjdieialskd")
    private String accessToken;

    @Schema(example = "true")
    private boolean isAlreadyExist;

    @Builder
    public TokenResponse(final String accessToken, final boolean isAlreadyExist) {
        this.accessToken = accessToken;
        this.isAlreadyExist = isAlreadyExist;
    }

    public static TokenResponse of(final String accessToken, final boolean isAlreadyExist) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .isAlreadyExist(isAlreadyExist)
                .build();
    }
}
