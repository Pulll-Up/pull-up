package com.pullup.auth.oAuth.domain.impl;

import com.pullup.auth.oAuth.domain.OAuth2UserInfo;
import com.pullup.auth.oAuth.domain.type.OAuthProvider;
import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo {
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    @SuppressWarnings("unchecked")
    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String getName() {
        return profile.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return profile.get("profile_image_url").toString();
    }
}

