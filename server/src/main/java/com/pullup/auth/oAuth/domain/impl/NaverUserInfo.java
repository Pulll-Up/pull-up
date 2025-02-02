package com.pullup.auth.oAuth.domain.impl;

import com.pullup.auth.oAuth.domain.OAuth2UserInfo;
import com.pullup.auth.oAuth.domain.type.OAuthProvider;
import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

    @SuppressWarnings("unchecked")
    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getProfileImageUrl() {
        return attributes.get("profile_image").toString();
    }
}
