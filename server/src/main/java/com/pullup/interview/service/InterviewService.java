package com.pullup.interview.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.domain.InterviewHint;
import com.pullup.interview.dto.InterviewAnswerDto;
import com.pullup.interview.dto.MyInterviewAnswerDto;
import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.repository.InterviewAnswerRepository;
import com.pullup.interview.repository.InterviewHintRepository;
import com.pullup.interview.repository.InterviewRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewService {
    private final MemberService memberService;
    private final LikeService likeService;
    private final CommentService commentService;
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

    public MyInterviewAnswerResponse submitInterviewAnswer(
            Long memberId,
            Long interviewId,
            MyInterviewAnswerRequest myInterviewAnswerRequest
    ) {
        Member member = memberService.findMemberById(memberId);

        Interview interview = interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        InterviewAnswer interviewAnswer = interviewAnswerRepository.save(InterviewAnswer.createInterviewAnswer(
                member,
                interview,
                myInterviewAnswerRequest.answer()
        ));

        return MyInterviewAnswerResponse.of(interviewId, interviewAnswer.getId());
    }

    public MyInterviewAnswersResponse getMyInterviewAnswers(Long memberId) {
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.findAllByMemberIdAndInterview(memberId);

        if (interviewAnswers.isEmpty()) {
            throw new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND);
        }

        List<MyInterviewAnswerDto> myInterviewAnswerDtos = interviewAnswers.stream()
                .map(answer -> MyInterviewAnswerDto.of(
                        answer.getInterview().getId(),
                        answer.getId(),
                        answer.getInterview().getQuestion()
                ))
                .collect(Collectors.toList());

        return MyInterviewAnswersResponse.of(myInterviewAnswerDtos);
    }

    public InterviewAnswersResponse getInterviewAnswers(Long memberId, Long interviewId) {
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.findAllByInterviewId(interviewId);

        if (interviewAnswers.isEmpty()) {
            throw new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND);
        }

        List<InterviewAnswerDto> interviewAnswerDtos = interviewAnswers.stream()
                .map(interviewAnswer -> InterviewAnswerDto.of(
                        interviewAnswer.getId(),
                        interviewAnswer.getMember().getName(),
                        interviewAnswer.getCreatedAt(),
                        interviewAnswer.getAnswer(),
                        likeService.isLikedInterviewAnswerByMember(memberId, interviewAnswer.getId()),
                        likeService.getLikesCount(interviewAnswer.getId()),
                        commentService.getCommentsCount(interviewAnswer.getId())
                ))
                .toList();

        return InterviewAnswersResponse.of(interviewAnswerDtos);
    }
}
