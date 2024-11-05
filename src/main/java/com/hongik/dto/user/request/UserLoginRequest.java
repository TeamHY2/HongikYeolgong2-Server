package com.hongik.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserLoginRequest {

    @Schema(example = "email@email.com")
    private String username;

    @Schema(example = "비밀번호")
    private String password;

    @Builder
    public UserLoginRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
