package com.pullup.auth.oAuth.service;

import com.pullup.auth.jwt.util.CookieUtil;
import com.pullup.auth.jwt.util.JwtUtil;
import com.pullup.auth.oAuth.dto.request.SignUpRequest;
import com.pullup.auth.oAuth.dto.response.LoginResponse;
import com.pullup.common.exception.BadRequestException;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.util.SecurityUtil;
import com.pullup.member.service.MemberService;
import com.pullup.member.service.facade.MemberFacade;
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
    private final MemberFacade memberFacade;
    private final MemberService memberService;

    public LoginResponse signIn(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = CookieUtil.extractTokenFromCookie(request, "access_token")
                .orElseThrow(() -> new BadRequestException(ErrorMessage.ERR_COOKIE_NOT_FOUND));

        jwtUtil.extractAccessTokenFromCookieAndIssueAccessTokenInHeader(accessToken, response);

        Long memberId = jwtUtil.resolveMemberIdFromJwtToken(accessToken);
        SecurityUtil.createAuthentication(memberId);

        return getLoginResponse(memberId);
    }

    public LoginResponse getLoginResponse(Long memberId) {
        String email = memberService.getMemberEmail(memberId);
        if (!memberService.isExistInterestSubjects(memberId)) {
            memberService.saveMemberExamStatistic(memberId);
            memberService.saveMemberGameResult(memberId);
            return LoginResponse.isFirstLogin(email);
        }
        if (!memberService.isSolvedToday(memberId)) {
            return LoginResponse.isNotFirstLoginAndNotSolvedToday(email);
        }
        String encryptedTodayInterviewAnswerId = memberFacade.getEncryptedTodayInterviewAnswerId(memberId);
        return LoginResponse.isNotFirstLoginAndSolvedToday(email, encryptedTodayInterviewAnswerId);
    }

    public void signUp(Long memberId, SignUpRequest singUpRequest) {
        memberService.saveInterestSubjects(memberId, singUpRequest.subjectNames());
    }
}