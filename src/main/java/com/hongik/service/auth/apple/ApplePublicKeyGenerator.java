package com.hongik.service.auth.apple;

import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyGenerator {

    public PublicKey generatePublicKeyWithApplePublicKeys(Map<String, String> tokenHeaders,
                                                          ApplePublicKeys applePublicKeys) {
        ApplePublicKey applePublicKey = applePublicKeys.getMatchedKey(tokenHeaders.get("kid"), tokenHeaders.get("alg"));

        byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.getE());

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(
                new BigInteger(1, nBytes), new BigInteger(1, eBytes)
        );
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AppException(ErrorCode.INVALID_JWT_EXCEPTION, ErrorCode.INVALID_JWT_EXCEPTION.getMessage());
        }
    }

}
