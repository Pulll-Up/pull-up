package com.pullup.common.util;

import com.pullup.auth.jwt.domain.TokenType;
import com.pullup.auth.jwt.exception.JwtAuthenticationException;
import com.pullup.auth.jwt.exception.JwtExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static void createAuthentication(Long memberId) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                memberId,
                null
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication == null) || !(authentication.isAuthenticated())) {
            throw new JwtAuthenticationException(JwtExceptionMessage.ERR_NOT_AUTHENTICATED_MEMBER,
                    TokenType.ACCESS_TOKEN);
        }
        return true;
    }
}
