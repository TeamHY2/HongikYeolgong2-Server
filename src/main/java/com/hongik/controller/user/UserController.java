package com.hongik.controller.user;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.user.request.NicknameRequest;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.request.UsernameRequest;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller - 유저 컨트롤러", description = "회원가입, 닉네임 중복검사를 진행합니다.")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> singUp(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.ok(userService.signUp(request));
    }

    @Operation(summary = "닉네임 중복검사", description = "닉네임 중복검사입니다.")
    @GetMapping("/duplicate-nickname")
    public void duplicateNickname(@Valid @RequestBody NicknameRequest request) {
        userService.checkNicknameDuplication(request.getNickname());
    }

    @Operation(summary = "이메일 중복검사", description = "이메일 중복검사입니다.")
    @PostMapping("/duplicate-username")
    public void duplicateUsername(@Valid @RequestBody UsernameRequest request) {
        userService.checkUsernameDuplication(request.getUsername());
    }

}
