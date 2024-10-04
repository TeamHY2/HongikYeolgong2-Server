package com.hongik.dto.auth.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Getter
public class LoginRequest {

    private String socialPlatform;

    private String name;

    private String code;

    @Builder
    public LoginRequest(final String socialPlatform, final String name, final String code) {
        this.socialPlatform = socialPlatform;
        this.name = name;
        this.code = code;
    }
}
