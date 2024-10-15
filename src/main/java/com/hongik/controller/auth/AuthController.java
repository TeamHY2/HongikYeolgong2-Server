package com.hongik.controller.auth;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.auth.request.AppleLoginRequest;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.TokenResponse;
import com.hongik.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "구글 소셜 로그인", description = "구글, idToken을 넣어주세요.")
    @PostMapping("/login")
    public ApiResponse<TokenResponse> selectGoogleLoginInfo(@RequestBody LoginRequest request){
        return ApiResponse.ok(authService.login(request));
    }
//    @Operation(summary = "구글, 애플 소셜 로그인", description = "구글과 애플, id_token을 넣어주세요.")
//    @PostMapping("/login")
//    public ResponseEntity<TokenResponse> selectGoogleLoginInfo(@RequestBody LoginRequest request){
//        return ResponseEntity.ok(authService.login(request));
//    }

    @Operation(summary = "애플 소셜 로그인", description = "애플, idToken, email을 넣어주세요.")
    @PostMapping("/login-apple")
    public ApiResponse<TokenResponse> selectAppleLoginInfo(@RequestBody AppleLoginRequest request){
        return ApiResponse.ok(authService.appleLogin(request));
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴 기능입니다. 현재 HardDelete")
    @DeleteMapping
    public void deleteUser(Authentication authentication){
        Long userId = Long.parseLong(authentication.getName());
        authService.deleteUser(userId);
    }
}
