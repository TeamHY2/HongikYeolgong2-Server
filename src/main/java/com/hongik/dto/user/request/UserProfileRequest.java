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
public class UserProfileRequest {

    @Schema(example = "닉네임")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{2,8}$", message = "띄어쓰기, 특수문자는 불가능하고, 2~8자까지 허용합니다.")
    private String nickname;

    @Schema(example = "디자인학부")
    @NotBlank(message = "학과는 필수입니다.")
    private String department;

    @Builder
    public UserProfileRequest(final String nickname, final String department) {
        this.nickname = nickname;
        this.department = department;
    }
}
