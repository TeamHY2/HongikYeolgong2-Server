package com.hongik.dto.auth.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoogleIdTokenRequest {

    private String id_token;

    @Builder
    public GoogleIdTokenRequest(final String id_token) {
        this.id_token = id_token;
    }
}
