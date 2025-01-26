package com.pullup.auth.OAuth.controller;

import com.pullup.auth.OAuth.dto.response.LoginResponse;
import com.pullup.auth.OAuth.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class OAuthController implements OAuthApi {
    private final OAuthService oAuthService;

    @Override
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signIn(HttpServletRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = oAuthService.signIn(request, response);
        return ResponseEntity.ok().body(loginResponse);
    }
}
