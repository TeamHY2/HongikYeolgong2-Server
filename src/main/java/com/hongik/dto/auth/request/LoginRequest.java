package com.hongik.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginRequest {

    @Schema(example = "google, apple")
    private String socialPlatform;

    private String id_token;

    @Builder
    public LoginRequest(final String socialPlatform, final String id_token) {
        this.socialPlatform = socialPlatform;
        this.id_token = id_token;
    }
}
