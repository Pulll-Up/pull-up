package com.pullup.auth.OAuth.domain;

import com.pullup.auth.OAuth.domain.impl.GoogleUserInfo;
import com.pullup.auth.OAuth.domain.impl.KakaoUserInfo;
import com.pullup.auth.OAuth.domain.impl.NaverUserInfo;
import com.pullup.common.exception.BadRequestException;
import com.pullup.common.exception.ErrorMessage;
import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch (registrationId) {
            case "google":
                return new GoogleUserInfo(attributes);
            case "naver":
                return new NaverUserInfo(attributes);
            case "kakao":
                return new KakaoUserInfo(attributes);
            default:
                throw new BadRequestException(ErrorMessage.ERR_UNSUPPORTED_OAUTH2_PROVIDER);
        }
    }
}
