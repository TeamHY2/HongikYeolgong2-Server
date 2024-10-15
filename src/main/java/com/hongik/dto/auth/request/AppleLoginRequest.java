package com.hongik.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AppleLoginRequest {

    @Schema(example = "email@icloud.com")
    private String email;

    private String idToken;

    @Builder
    public AppleLoginRequest(final String email, final String idToken) {
        this.email = email;
        this.idToken = idToken;
    }
}
