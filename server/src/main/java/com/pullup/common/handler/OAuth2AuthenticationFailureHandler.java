package com.pullup.common.handler;

import static com.pullup.common.exception.ErrorMessage.ERR_FAILURE_OAUTH2_AUTHENTICATION;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        log.error("OAuth2 로그인 실패: {}", exception.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ERR_FAILURE_OAUTH2_AUTHENTICATION.getMessage());
        response.sendRedirect("http://localhost:5173");
    }
}
