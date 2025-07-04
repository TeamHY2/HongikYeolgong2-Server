package com.hongik.dto.user.response;

import com.hongik.domain.user.User;
import com.hongik.dto.user.request.UserDeviceTokenRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDeviceTokenResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "email@email.com")
    private String username;

    @Schema(example = "닉네임")
    private String nickname;

    @Schema(example = "디바이스 토큰")
    private String deviceToken;

    @Builder
    public UserDeviceTokenResponse(final Long id, final String username, final String nickname, final String deviceToken) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.deviceToken = deviceToken;
    }

    public static UserDeviceTokenResponse of(User user) {
        return UserDeviceTokenResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .deviceToken(user.getDeviceToken())
                .build();
    }
}
