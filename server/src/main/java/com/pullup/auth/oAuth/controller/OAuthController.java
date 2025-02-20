package com.pullup.auth.oAuth.controller;

import com.pullup.auth.oAuth.dto.request.SignUpRequest;
import com.pullup.auth.oAuth.dto.response.LoginResponse;
import com.pullup.auth.oAuth.service.OAuthService;
import com.pullup.common.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loginResponse);
    }

    @Override
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        oAuthService.signUp(memberId, signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @Override
    @GetMapping("/check")
    public ResponseEntity<LoginResponse> checkSignIn(HttpServletRequest request, HttpServletResponse response) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        LoginResponse loginResponse = oAuthService.getLoginResponse(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponse);
    }
}