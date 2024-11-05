package com.hongik.dto.auth.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoogleInfoResponse {

    private String sub;

    private String name;

    private String email;

    @Builder
    public GoogleInfoResponse(final String sub, final String name, final String email) {
        this.sub = sub;
        this.name = name;
        this.email = email;
    }

    public static GoogleInfoResponse of(final String sub, final String name, final String email) {
        return GoogleInfoResponse.builder()
                .sub(sub)
                .name(name)
                .email(email)
                .build();
    }
}
