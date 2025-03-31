package com.hongik.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeviceTokenResponse {

    @Schema(example = "deviceToken")
    private String deviceToken;

    @Builder
    public DeviceTokenResponse(final String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public static DeviceTokenResponse of(final String deviceToken) {
        return DeviceTokenResponse.builder()
                .deviceToken(deviceToken)
                .build();
    }
}
