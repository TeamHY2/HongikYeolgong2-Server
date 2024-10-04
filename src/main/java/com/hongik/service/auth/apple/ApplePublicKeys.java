package com.hongik.service.auth.apple;

import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApplePublicKeys {

    private List<ApplePublicKey> keys = new ArrayList<>();

    @Builder
    public ApplePublicKeys(final List<ApplePublicKey> keys) {
        this.keys = keys;
    }

    public ApplePublicKey getMatchedKey(String kid, String alg) throws AppException {
        return keys.stream()
                .filter(applePublicKey -> applePublicKey.getKid().equals(kid) && applePublicKey.getAlg().equals(alg))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_JWT_EXCEPTION, ErrorCode.INVALID_JWT_EXCEPTION.getMessage()));
    }
}
