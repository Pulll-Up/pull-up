package com.pullup.auth.oAuth.domain;


import com.pullup.auth.oAuth.domain.type.OAuthProvider;
import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getProviderId();

    public abstract OAuthProvider getProvider();

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getProfileImageUrl();
}