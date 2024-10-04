package com.hongik.service.auth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Component
public class AppleIdentityTokenParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Map<String, String> parseHeaders(String identityToken) {
        try {
            String encoded = identityToken.split("\\.")[0];
            String decoded = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
            return OBJECT_MAPPER.readValue(decoded, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            throw new AppException(ErrorCode.INVALID_JWT_EXCEPTION, ErrorCode.INVALID_JWT_EXCEPTION.getMessage());
        }
    }

    public Claims parseWithPublicKeyAndGetClaims(String identityToken, PublicKey publicKey) {
        try {
            return getJwtParser(publicKey)
                    .parseSignedClaims(identityToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.INVALID_EXPIRATION_JWT_EXCEPTION, ErrorCode.INVALID_EXPIRATION_JWT_EXCEPTION.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_JWT_EXCEPTION, ErrorCode.INVALID_JWT_EXCEPTION.getMessage());
        }
    }

    private JwtParser getJwtParser(Key key) {
        return Jwts.parser()
                .verifyWith((PublicKey) key)
                .build();
    }
}
