package com.hongik.dto.auth.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GoogleInfoResponse {

    private String id;

    private String name;

    private String email;

    @Builder
    public GoogleInfoResponse(final String id, final String name, final String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static GoogleInfoResponse of(final String id, final String name, final String email) {
        return GoogleInfoResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }
}
