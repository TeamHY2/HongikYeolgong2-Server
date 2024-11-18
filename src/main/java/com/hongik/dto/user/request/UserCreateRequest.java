package com.hongik.dto.user.request;

import com.hongik.domain.user.Role;
import com.hongik.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateRequest {

    @Schema(example = "email@email.com")
    @NotBlank(message = "이메일은 필수입니다.")
    private String username;

    @Schema(example = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Schema(example = "닉네임")
    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]{2,8}$", message = "띄어쓰기, 숫자, 특수문자는 불가능하고, 2~8자까지 허용합니다.")
    private String nickname;

    @Schema(example = "디자인학부")
    @NotBlank(message = "학과는 필수입니다.")
    private String department;

    @Builder
    public UserCreateRequest(final String username, final String password, final String nickname, final String department) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.department = department;
    }

    public User toEntity(final String password) {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .nickname(nickname)
                .department(department)
                .build();
    }
}
