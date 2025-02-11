package com.pullup.member.service.facade;

import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.service.InterviewService;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {
    private final MemberService memberService;
    private final InterviewService interviewService;

    @Transactional
    public MyInterviewAnswerResponse submitInterviewAnswer(
            Long memberId,
            Long interviewId,
            MyInterviewAnswerRequest myInterviewAnswerRequest
    ) {
        Member member = memberService.findMemberById(memberId);

        Interview interview = interviewService.findInterviewById(interviewId);

        //TODO : GPT API 연동 후, 강점, 약점 분석 로직 추가

        memberService.updateSolveStatus(member);
        InterviewAnswer interviewAnswer = interviewService.saveInterviewAnswer(member, interview,
                myInterviewAnswerRequest.answer());

        return MyInterviewAnswerResponse.of(interviewId, interviewAnswer.getId());
    }

    public Long getTodayInterviewAnswerId(Long memberId) {
        return interviewService.getTodayInterviewAnswerId(memberId);
    }
}
