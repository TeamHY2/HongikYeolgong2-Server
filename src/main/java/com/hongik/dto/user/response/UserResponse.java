package com.hongik.dto.user.response;

import com.hongik.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "email@email.com")
    private String username;

    @Schema(example = "닉네임")
    private String nickname;

    @Builder
    public UserResponse(final Long id, final String username, final String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
