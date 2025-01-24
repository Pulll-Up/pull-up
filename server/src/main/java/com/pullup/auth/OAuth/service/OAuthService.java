package com.pullup.auth.OAuth.service;

import com.pullup.auth.OAuth.dto.response.LoginResponse;
import com.pullup.auth.jwt.util.CookieUtil;
import com.pullup.auth.jwt.util.JwtUtil;
import com.pullup.common.exception.BadRequestException;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final JwtUtil jwtUtil;

    public LoginResponse signIn(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.extractAccessTokenFromCookie(request)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.ERR_COOKIE_NOT_FOUND));
        jwtUtil.extractAccessTokenFromCookieAndIssueAccessTokenInHeader(accessToken, response);
        Long memberId = jwtUtil.resolveMemberIdFromJwtToken(accessToken);
        SecurityUtil.createAuthentication(memberId);

        return new LoginResponse(true, true);
    }
}
