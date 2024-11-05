package com.hongik.controller.token;

import com.hongik.dto.ApiResponse;
import com.hongik.dto.token.response.TokenValidationResponse;
import com.hongik.swagger.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.hongik.exception.ErrorCode.*;

@Tag(name = "Token Controller - 토큰 컨트롤러", description = "토큰 유효성 및 유저의 권한을 응답합니다.")
@RequestMapping("/api/v1/token")
@RestController
public class TokenController {

    @ApiErrorCodeExamples({INVALID_INPUT_VALUE, INVALID_JWT_EXCEPTION, INVALID_EXPIRATION_JWT_EXCEPTION})
    @Operation(summary = "토큰 유효성 및 유저 권한 응답", description = "토큰이 유효하지 않으면 예외가 발생합니다.")
    @GetMapping
    public ApiResponse<TokenValidationResponse> validateToken(Authentication authentication) {
        String role = "";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority().split("_")[1];
        }

        return ApiResponse.ok(TokenValidationResponse.of(true, role));
    }
}
