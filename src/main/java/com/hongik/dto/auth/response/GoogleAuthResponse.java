package com.hongik.dto.auth.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoogleAuthResponse {

    private String access_token;

    private String id_token;

    @Builder
    public GoogleAuthResponse(final String access_token, final String id_token) {
        this.access_token = access_token;
        this.id_token = id_token;
    }

    public static GoogleAuthResponse of(final String access_token, final String id_token) {
        return GoogleAuthResponse.builder()
                .access_token(access_token)
                .id_token(id_token)
                .build();
    }
}
