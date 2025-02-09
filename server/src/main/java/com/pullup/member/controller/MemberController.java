package com.pullup.member.controller;

import com.pullup.common.util.SecurityUtil;
import com.pullup.member.dto.request.DeviceTokenRequest;
import com.pullup.member.dto.request.InterestSubjectsRequest;
import com.pullup.member.dto.response.MemberProfileResponse;
import com.pullup.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<MemberProfileResponse> getMemberProfile() {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        MemberProfileResponse memberProfileResponse = memberService.getMemberProfile(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberProfileResponse);
    }

    @Override
    @PatchMapping("/interest-subject")
    public ResponseEntity<Void> updateInterestSubject(
            @Valid @RequestBody InterestSubjectsRequest interestSubjectsRequest) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        memberService.updateInterestSubject(memberId, interestSubjectsRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Override
    @PostMapping("/device-token")
    public ResponseEntity<Void> registerDeviceToken(@Valid @RequestBody DeviceTokenRequest deviceToken) {
        Long memberId = SecurityUtil.getAuthenticatedMemberId();
        memberService.registerDeviceToken(memberId, deviceToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}