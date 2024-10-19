package com.hongik.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NicknameResponse {

    @Schema(example = "nickname")
    private String nickname;

    @Schema(example = "false")
    private boolean isDuplicate;

    @Builder
    public NicknameResponse(final String nickname, final boolean isDuplicate) {
        this.nickname = nickname;
        this.isDuplicate = isDuplicate;
    }

    public static NicknameResponse of(final String nickname, final boolean isDuplicate) {
        return NicknameResponse.builder()
                .nickname(nickname)
                .isDuplicate(isDuplicate)
                .build();
    }
}
