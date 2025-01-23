package com.pullup.auth.OAuth.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    GOOGLE("구글"),
    NAVER("네이버"),
    KAKAO("카카오");

    private final String name;
}