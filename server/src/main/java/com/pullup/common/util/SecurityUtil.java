package com.pullup.common.util;

import com.pullup.auth.jwt.domain.TokenType;
import com.pullup.auth.jwt.exception.JwtAuthenticationException;
import com.pullup.auth.jwt.exception.JwtExceptionMessage;
import com.pullup.member.type.Role;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Long getAuthenticatedMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new JwtAuthenticationException(JwtExceptionMessage.ERR_NOT_AUTHENTICATED_MEMBER,
                    TokenType.ACCESS_TOKEN);
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Long)) {
            throw new RuntimeException(JwtExceptionMessage.ERR_INVALID_PRINCIPAL_TYPE.getMessage());
        }
        return (Long) principal;
    }

    public static void createAuthentication(Long memberId) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                memberId,
                null,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + Role.USER))
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
