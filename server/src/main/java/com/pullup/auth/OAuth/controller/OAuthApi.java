package com.pullup.auth.OAuth.controller;


import com.pullup.auth.OAuth.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface OAuthApi {

    @ApiResponse(responseCode = "200", description = "로그인 성공")
    ResponseEntity<LoginResponse> signIn(HttpServletRequest request, HttpServletResponse response);
}
