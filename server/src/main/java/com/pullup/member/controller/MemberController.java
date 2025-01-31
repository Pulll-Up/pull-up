package com.pullup.member.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.member.dto.response.MemberProfileResponse;
import com.pullup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<MemberProfileResponse> getMemberProfile() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        MemberProfileResponse memberProfileResponse = memberService.getMemberProfile(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberProfileResponse);
    }
}
