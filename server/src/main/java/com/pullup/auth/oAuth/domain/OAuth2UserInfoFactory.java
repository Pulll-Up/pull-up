package com.pullup.auth.oAuth.domain;

import com.pullup.auth.oAuth.domain.impl.GoogleUserInfo;
import com.pullup.auth.oAuth.domain.impl.KakaoUserInfo;
import com.pullup.auth.oAuth.domain.impl.NaverUserInfo;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.IllegalArgumentException;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        return OAuth2UserInfoFactory.determineOAuth2UserInfo(
                userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );
    }

    private static OAuth2UserInfo determineOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> new GoogleUserInfo(attributes);
            case "naver" -> new NaverUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
            default -> throw new IllegalArgumentException(ErrorMessage.ERR_UNSUPPORTED_OAUTH2_PROVIDER);
        };
    }
}
