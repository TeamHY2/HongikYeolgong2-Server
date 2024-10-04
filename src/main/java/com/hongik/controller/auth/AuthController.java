package com.hongik.controller.auth;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.dto.auth.response.TokenResponse;
import com.hongik.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login-view")
    public String getGoogleLoginView(){
        return authService.getGoogleLoginView();
    }

//    @GetMapping("/login")
//    public ApiResponse<TokenResponse> selectGoogleLoginInfo(@RequestBody LoginRequest request){
//        System.out.println("\"gdgdgd\" = " + "gdgdgd");
//        return ApiResponse.ok(authService.login(request));
//    }
    @GetMapping("/login")
    public ApiResponse<TokenResponse> selectGoogleLoginInfo(@RequestParam String code){
        System.out.println("\"gdgdgd\" = " + "gdgdgd");
        System.out.println("code = " + code);
        LoginRequest request = LoginRequest.builder()
                .name("병일")
                .socialPlatform("google")
                .code(code).build();
        return ApiResponse.ok(authService.login(request));
//        return null;
    }
}
