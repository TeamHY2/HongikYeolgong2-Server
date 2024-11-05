package com.hongik.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UsernameRequest {

    @Schema(example = "test@email.com")
    @NotBlank(message = "이메일은 필수입니다.")
    private String username;

    @Builder
    public UsernameRequest(final String username) {
        this.username = username;
    }
}
