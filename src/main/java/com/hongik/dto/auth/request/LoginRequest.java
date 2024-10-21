package com.hongik.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@Getter
public class LoginRequest {

    @Schema(example = "eya2bsd")
    @NotBlank(message = "idToken은 필수입니다.")
    private String idToken;

    @Builder
    public LoginRequest(final String idToken) {
        this.idToken = idToken;
    }
}
