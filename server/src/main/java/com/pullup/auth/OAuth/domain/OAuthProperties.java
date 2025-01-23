package com.pullup.auth.OAuth.domain;

import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuthProperties {
    private Map<String, Registration> registration;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Provider {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String authorizationGrantType;
        private String clientName;
        private Set<String> scope;
    }
}
