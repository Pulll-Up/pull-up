package com.pullup.common.filter;

import static com.pullup.auth.jwt.exception.JwtExceptionMessage.ERR_NOT_EXISTS_JWT;

import com.pullup.auth.jwt.config.JwtConstants;
import com.pullup.auth.jwt.domain.JwtTokenValidator;
import com.pullup.auth.jwt.domain.TokenType;
import com.pullup.auth.jwt.exception.CustomAuthenticationEntryPoint;
import com.pullup.auth.jwt.exception.JwtAuthenticationException;
import com.pullup.auth.jwt.util.CookieUtil;
import com.pullup.auth.jwt.util.JwtUtil;
import com.pullup.common.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String REISSUE_API_URL = "/api/v1/auth/reissue";
    private static final String LOGOUT_API_URL = "/api/v1/auth/logout";

    private final JwtUtil jwtUtil;
    private final JwtTokenValidator jwtTokenValidator;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (isReissueRequest(request)) {
                handleRefreshToken(request, response);
            } else if (isLogoutRequest(request)) {
                handleLogout(request, response);
            } else {
                handleAccessToken(request);
            }
        } catch (JwtAuthenticationException e) {
            customAuthenticationEntryPoint.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isReissueRequest(HttpServletRequest request) {
        return REISSUE_API_URL.equals(request.getRequestURI());
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return LOGOUT_API_URL.equals(request.getRequestURI());
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response)
            throws JwtAuthenticationException {
        String refreshToken = jwtUtil.resolveRefreshTokenFromCookie(request);
        jwtTokenValidator.validateJwtToken(refreshToken, TokenType.REFRESH_TOKEN);

        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            throw new JwtAuthenticationException(ERR_NOT_EXISTS_JWT, TokenType.REFRESH_TOKEN);
        }
        jwtUtil.issueAccessTokenAndRefreshToken(jwtUtil.resolveMemberIdFromJwtToken(refreshToken), response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtil.isAuthenticated()) {
            return;
        }
        ResponseCookie responseCookie = CookieUtil.deleteTokenAtCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME);
        response.addHeader("set-cookie", responseCookie.toString());
        jwtUtil.clearAuthenticationAndCookies(request, response);
    }


    private void handleAccessToken(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveAccessTokenFromHeader(request);
        jwtTokenValidator.validateJwtToken(accessToken, TokenType.ACCESS_TOKEN);
        SecurityUtil.createAuthentication(jwtUtil.resolveMemberIdFromJwtToken(accessToken));
    }
}
