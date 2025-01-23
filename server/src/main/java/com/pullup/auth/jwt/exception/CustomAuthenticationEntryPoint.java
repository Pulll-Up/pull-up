package com.pullup.auth.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullup.common.exception.FailResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        log.warn("[Unauthorized error] {}: {}", authException.getClass().getName(), authException.getMessage());
        sendErrorResponse(response, HttpStatus.UNAUTHORIZED, authException.getMessage());
    }

    public void sendErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message
    ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status.value());

        FailResponse apiResponse = FailResponse.fail(status.value(), message);
        response.getWriter().println(objectMapper.writeValueAsString(apiResponse));
    }
}
