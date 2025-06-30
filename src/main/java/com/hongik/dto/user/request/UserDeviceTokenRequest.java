package com.hongik.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDeviceTokenRequest {

    @Schema(example = "디바이스 토큰")
    @NotBlank(message = "디바이스 토큰은 필수입니다.")
    private String deviceToken;

    @Builder
    public UserDeviceTokenRequest(final String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
