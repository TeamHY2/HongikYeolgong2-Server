package com.hongik.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SocialPlatform {

    GOOGLE("GOOGLE"),
    APPLE("APPLE");

    private final String socialPlatform;
}
