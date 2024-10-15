package com.hongik.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@Getter
public class LoginRequest {

    @Schema(example = "google, apple")
    private String socialPlatform;

    private String idToken;

    @Builder
    public LoginRequest(final String socialPlatform, final String idToken) {
        this.socialPlatform = socialPlatform;
        this.idToken = idToken;
    }
}
