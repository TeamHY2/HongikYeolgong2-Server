package com.hongik.controller.user;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.user.request.UserCreateRequest;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> singUp(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.ok(userService.signUp(request));
    }
}
