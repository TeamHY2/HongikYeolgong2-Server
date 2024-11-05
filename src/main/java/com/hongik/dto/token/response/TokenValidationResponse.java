package com.hongik.dto.token.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenValidationResponse {

    @Schema(example = "true")
    private boolean isValidToken;

    @Schema(example = "USER")
    private String Role;

    @Builder
    public TokenValidationResponse(final boolean isValidToken, final String role) {
        this.isValidToken = isValidToken;
        Role = role;
    }

    public static TokenValidationResponse of(final boolean isValidToken, final String role) {
        return TokenValidationResponse.builder()
                .isValidToken(isValidToken)
                .role(role)
                .build();
    }
}
