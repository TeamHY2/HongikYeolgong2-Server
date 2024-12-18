package com.hongik.controller.auth;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.auth.request.AppleLoginRequest;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.TokenResponse;
import com.hongik.dto.user.response.UserResponse;
import com.hongik.exception.ErrorCode;
import com.hongik.service.auth.AuthService;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.hongik.exception.ErrorCode.*;

@Tag(name = "Auth Controller - 소셜 로그인 컨트롤러", description = "로그인할 때 Apple 또는 Google, id_token을 넣어주세요.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

//    @GetMapping("/login-view")
//    public String getGoogleLoginView(){
//        return authService.getGoogleLoginView();
//    }


    @ApiErrorCodeExamples({INTERNAL_SERVER_ERROR, INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE})
    @Operation(summary = "구글 소셜 로그인", description = "idToken을 넣어주세요.")
    @PostMapping("/login-google")
    public ApiResponse<TokenResponse> selectGoogleLoginInfo(@Valid @RequestBody LoginRequest request){
        return ApiResponse.ok(authService.googleLogin(request));
    }
//    @Operation(summary = "구글, 애플 소셜 로그인", description = "구글과 애플, id_token을 넣어주세요.")
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> selectGoogleLoginInfo(@RequestBody LoginRequest request){
//        return ResponseEntity.ok(authService.login(request));
//    }

    @ApiErrorCodeExamples({INTERNAL_SERVER_ERROR, INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE})
    @Operation(summary = "애플 소셜 로그인", description = "애플, idToken, email을 넣어주세요.")
    @PostMapping("/login-apple")
    public ApiResponse<TokenResponse> selectAppleLoginInfo(@RequestBody AppleLoginRequest request){
        return ApiResponse.ok(authService.appleLogin(request));
    }

    @ApiErrorCodeExamples({INVALID_JWT_EXCEPTION, INVALID_INPUT_VALUE, REGISTRATION_INCOMPLETE, NOT_FOUND_USER})
    @Operation(summary = "회원탈퇴", description = "회원탈퇴 기능입니다. 현재 HardDelete")
    @DeleteMapping
    public ApiResponse<UserResponse> deleteUser(Authentication authentication){
        Long userId = Long.parseLong(authentication.getName());
        return ApiResponse.ok(authService.deleteUser(userId));
    }
}
