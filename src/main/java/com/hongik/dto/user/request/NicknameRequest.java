package com.hongik.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NicknameRequest {

    @Schema(example = "닉네임")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{2,8}$", message = "띄어쓰기, 특수문자는 불가능하고, 2~8자까지 허용합니다.")
    private String nickname;

    @Builder
    public NicknameRequest(final String nickname) {
        this.nickname = nickname;
    }
}
