package com.hongik.controller.auth;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.TokenResponse;
import com.hongik.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Controller - 소셜 로그인 컨트롤러", description = "Apple, Google와 code를 넣어주세요.")
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

//    @GetMapping("/login-view")
//    public String getGoogleLoginView(){
//        return authService.getGoogleLoginView();
//    }

//    @GetMapping("/login")
//    public ApiResponse<TokenResponse> selectGoogleLoginInfo(@RequestBody LoginRequest request){
//        System.out.println("\"gdgdgd\" = " + "gdgdgd");
//        return ApiResponse.ok(authService.login(request));
//    }

    @Operation(summary = "구글, 애플 소셜 로그인", description = "구글과 애플, code를 넣어주세요.")
    @GetMapping("/login")
    public ApiResponse<TokenResponse> selectGoogleLoginInfo(@RequestBody LoginRequest request){
        return ApiResponse.ok(authService.login(request));
    }
}
