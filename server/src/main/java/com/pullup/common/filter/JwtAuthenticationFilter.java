package com.pullup.common.filter;

import com.pullup.auth.jwt.JwtTokenValidator;
import com.pullup.auth.jwt.TokenType;
import com.pullup.auth.jwt.exception.CustomAuthenticationEntryPoint;
import com.pullup.auth.jwt.exception.JwtAuthenticationException;
import com.pullup.auth.jwt.util.JwtUtil;
import com.pullup.common.util.SecurityUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String REISSUE_API_URL = "/v1/member/reissue";
    private static final String LOGOUT_API_URL = "/v1/member/logout";

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
            if(isReissueRequest(request)){
                handleRefreshToken(request, response);
            } else if(isLogoutRequest(request)){
                //handleLogout(request, response);
            }else{
                handleAccessToken(request);
            }
        } catch (JwtAuthenticationException e) {
            customAuthenticationEntryPoint.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isReissueRequest(HttpServletRequest request){
        return REISSUE_API_URL.equals(request.getRequestURI());
    }

    private boolean isLogoutRequest(HttpServletRequest request){
        return LOGOUT_API_URL.equals(request.getRequestURI());
    }

    private void handleRefreshToken(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = jwtUtil.resolveRefreshTokenFromCookie(request);
        jwtTokenValidator.validateToken(refreshToken, TokenType.REFRESH_TOKEN);
        //TODO : 엑세스 토큰을 재발급하고 쿠키에 저장

    }

    private void handleAccessToken(HttpServletRequest request){
        String accessToken = jwtUtil.resolveAccessTokenFromHeader(request);
        jwtTokenValidator.validateToken(accessToken, TokenType.ACCESS_TOKEN);
        SecurityUtil.createAuthentication(jwtUtil.resolveMemberIdFromAccessToken(accessToken));
    }
}
