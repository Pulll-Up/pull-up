package com.pullup.auth.jwt.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String secret;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
    private String authRedirectUri;

    @PostConstruct
    public void logProperties() {
        System.out.println("JWT Properties Loaded:");
        System.out.println("Secret: " + secret);
        System.out.println("Access Token Expiration: " + accessTokenExpiration);
        System.out.println("Refresh Token Expiration: " + refreshTokenExpiration);
        System.out.println("Auth Redirect URI: " + authRedirectUri);
    }
}
