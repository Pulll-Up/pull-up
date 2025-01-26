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
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<RequestMatcher> LOGIN_PATHS = Arrays.asList(
            new AntPathRequestMatcher("/api/v1/auth/signin", HttpMethod.POST.toString())
    );

    private static final List<RequestMatcher> SWAGGER_PATHS = Arrays.asList(
            new AntPathRequestMatcher("/swagger-resources/**", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/favicon.ico", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/api-docs/**", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/swagger-ui.html", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/swagger-ui/index.html", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/docs/swagger-ui/index.html", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/swagger-ui/swagger-ui.css", HttpMethod.GET.toString()),
            new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.toString())
    );

    private static final RequestMatcher EXCLUDED_LOGIN_PATHS_REQUEST_MATCHER = new OrRequestMatcher(LOGIN_PATHS);
    private static final RequestMatcher EXCLUDED_SWAGGER_PATHS_REQUEST_MATCHER = new OrRequestMatcher(SWAGGER_PATHS);

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
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

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

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean shouldNotFilter = EXCLUDED_LOGIN_PATHS_REQUEST_MATCHER.matches(request);
        shouldNotFilter |= EXCLUDED_SWAGGER_PATHS_REQUEST_MATCHER.matches(request);
        log.info("Should not filter for request [{}]: {}", request.getRequestURI(), shouldNotFilter);
        return shouldNotFilter;
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
        ResponseCookie responseCookie = CookieUtil.createDeleteTokenAtCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME);
        response.addHeader("set-cookie", responseCookie.toString());
        jwtUtil.clearAuthenticationAndCookies(request, response);
    }


    private void handleAccessToken(HttpServletRequest request) {
        String accessToken = jwtUtil.resolveAccessTokenFromHeader(request);
        jwtTokenValidator.validateJwtToken(accessToken, TokenType.ACCESS_TOKEN);
        SecurityUtil.createAuthentication(jwtUtil.resolveMemberIdFromJwtToken(accessToken));
    }
}
