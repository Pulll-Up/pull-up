package com.pullup.interview.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.domain.InterviewHint;
import com.pullup.interview.dto.MyInterviewAnswerDto;
import com.pullup.interview.dto.request.InterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.repository.InterviewAnswerRepository;
import com.pullup.interview.repository.InterviewHintRepository;
import com.pullup.interview.repository.InterviewRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final MemberService memberService;
    private final InterviewRepository interviewRepository;
    private final InterviewHintRepository interviewHintRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;

    public InterviewResponse getTodayInterview(Long interviewId) {
        Interview interview = interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        List<InterviewHint> interviewHints = interviewHintRepository.findByInterviewId(interviewId);
        List<String> keywords = interviewHints.stream().map(InterviewHint::getKeyword).toList();

        return InterviewResponse.of(interview.getId(), interview.getQuestion(), keywords);
    }

    public InterviewAnswerResponse submitInterviewAnswer(
            Long MemberId,
            Long interviewId,
            InterviewAnswerRequest interviewAnswerRequest
    ) {
        Member member = memberService.findMemberById(MemberId);

        Interview interview = interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        InterviewAnswer interviewAnswer = interviewAnswerRepository.save(InterviewAnswer.createInterviewAnswer(
                member,
                interview,
                interviewAnswerRequest.answer()
        ));

        return InterviewAnswerResponse.of(interviewId, interviewAnswer.getId());
    }

    public MyInterviewAnswersResponse getMyInterviewAnswers(Long memberId) {
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.findAllByMemberId(memberId);
        List<MyInterviewAnswerDto> list = new ArrayList<>();

        return MyInterviewAnswersResponse.of(list);
    }
}
