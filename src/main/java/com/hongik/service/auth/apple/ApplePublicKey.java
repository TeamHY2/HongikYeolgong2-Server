package com.hongik.service.auth.apple;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ApplePublicKey {

    private String kty;
    private String alg;
    private String kid;
    private String n;
    private String e;

    @Builder
    public ApplePublicKey(final String kty, final String alg, final String kid, final String n, final String e) {
        this.kty = kty;
        this.alg = alg;
        this.kid = kid;
        this.n = n;
        this.e = e;
    }


}
