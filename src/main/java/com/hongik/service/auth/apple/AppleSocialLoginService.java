package com.hongik.service.auth.apple;

import com.hongik.dto.auth.request.LoginRequest;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleSocialLoginService {

    private final AppleFeignClient appleFeignClient;
    private final AppleIdentityTokenParser appleIdentityTokenParser;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleIdentityTokenValidator appleIdentityTokenValidator;

    public Claims login(LoginRequest socialLoginRequest) {
        Map<String, String> headers = appleIdentityTokenParser.parseHeaders(socialLoginRequest.getCode());
        ApplePublicKeys applePublicKeys = appleFeignClient.getApplePublicKeys();
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKeyWithApplePublicKeys(headers, applePublicKeys);
        Claims claims = appleIdentityTokenParser.parseWithPublicKeyAndGetClaims(socialLoginRequest.getCode(), publicKey);
        validateClaims(claims);

        return claims;

    }

    private void validateClaims(Claims claims) {
        if (!appleIdentityTokenValidator.isValidAppleIdentityToken(claims)) {
            throw new AppException(ErrorCode.INVALID_JWT_EXCEPTION, ErrorCode.INVALID_JWT_EXCEPTION.getMessage());
        }
    }
}
