package com.hongik.controller.user;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.user.request.NicknameRequest;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UserJoinRequest;
import com.hongik.dto.user.request.UsernameRequest;
import com.hongik.dto.user.response.NicknameResponse;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.service.user.UserService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongik.exception.ErrorCode.*;
import static com.hongik.exception.ErrorCode.NOT_FOUND_USER;

@Tag(name = "User Controller - 유저 컨트롤러", description = "회원가입, 닉네임 중복검사를 진행합니다.")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @Hidden
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> singUp(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.ok(userService.signUp(request));
    }

    @ApiErrorCodeExamples({INVALID_INPUT_VALUE})
    @Operation(summary = "닉네임 중복검사", description = "닉네임 중복검사입니다.")
    @GetMapping("/duplicate-nickname")
    public ApiResponse<NicknameResponse> duplicateNickname(@Pattern(regexp = "^[A-Za-z0-9가-힣]{2,8}$", message = "띄어쓰기, 특수문자는 불가능하고, 2~8자까지 허용합니다.") @NotBlank(message = "닉네임은 필수입니다.") @RequestParam String nickname) {
        return ApiResponse.ok(userService.checkNicknameDuplication(nickname));
    }

    @Hidden
    @Operation(summary = "이메일 중복검사", description = "이메일 중복검사입니다.")
    @PostMapping("/duplicate-username")
    public void duplicateUsername(@Valid @RequestBody UsernameRequest request) {
        userService.checkUsernameDuplication(request.getUsername());
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, NOT_FOUND_USER, ALREADY_EXIST_NICKNAME})
    @Operation(summary = "닉네임과 학과를 입력하여 회원가입합니다.", description = "닉네임과 학과는 필수이며, 이 과정을 거쳐야 서비스를 이용할 수 있습니다. <br> ROLE.GUEST -> ROLE.USER로 등록합니다.")
    @PostMapping("/join")
    public ApiResponse<UserResponse> join(@Valid @RequestBody UserJoinRequest request,
                                             Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(userService.join(request, userId));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "본인 프로필 정보 조회", description = "닉네임과 학과를 반환합니다.")
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUserInfo(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(userService.getUserInfo(userId));
    }
}
