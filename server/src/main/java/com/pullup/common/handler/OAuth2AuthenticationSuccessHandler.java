package com.pullup.common.handler;

import com.pullup.auth.oAuth.domain.PrincipalDetail;
import com.pullup.auth.jwt.domain.JwtToken;
import com.pullup.auth.jwt.util.CookieUtil;
import com.pullup.auth.jwt.util.JwtUtil;
import com.pullup.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements
        AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        Member member = principal.getMember();

        log.info("OAuth2 로그인 성공: {}", member.getEmail());

        JwtToken jwtToken = jwtUtil.generateJwtTokens(member.getId());
        setJwtTokenAtCookie(response, jwtToken);

        String accessToken = jwtToken.accessToken();
        log.info("Access Token: {}", accessToken);

        response.sendRedirect("http://localhost:5173/api/v1/auth/signin");
    }

    private static void setJwtTokenAtCookie(HttpServletResponse response, JwtToken jwtToken) {
        ResponseCookie accessTokenForCookie = CookieUtil.createAccessTokenForCookie(jwtToken.accessToken());
        ResponseCookie refreshTokenForCookie = CookieUtil.createRefreshTokenForCookie(jwtToken.refreshToken());
        log.info("Access Token: {}", jwtToken.accessToken());
        log.info("Refresh Token: {}", jwtToken.refreshToken());
        response.addHeader("set-cookie", accessTokenForCookie.toString());
        response.addHeader("set-cookie", refreshTokenForCookie.toString());
    }
}
