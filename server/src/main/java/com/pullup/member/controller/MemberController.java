package com.pullup.member.controller;

import com.pullup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private static final Long TEMP_MEMBER_ID = 1L;
    private final MemberService memberService;

    @PostMapping("/member-exam-statistic")
    public void postMemberExamStatistic() {
        memberService.saveMemberExamStatic(TEMP_MEMBER_ID);
    }

}
