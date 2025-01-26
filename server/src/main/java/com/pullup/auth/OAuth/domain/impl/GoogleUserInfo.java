package com.pullup.auth.OAuth.domain.impl;

import com.pullup.auth.OAuth.domain.OAuth2UserInfo;
import com.pullup.auth.OAuth.domain.type.OAuthProvider;
import java.util.Map;

public class GoogleUserInfo extends OAuth2UserInfo {
    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public OAuthProvider getProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }


    @Override
    public String getProfileImageUrl() {
        return (String) attributes.get("picture");
    }
}
