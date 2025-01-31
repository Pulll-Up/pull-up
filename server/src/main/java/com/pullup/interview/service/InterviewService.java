package com.pullup.interview.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewHint;
import com.pullup.interview.dto.request.InterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.repository.InterviewHintRepository;
import com.pullup.interview.repository.InterviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewHintRepository interviewHintRepository;

    public InterviewResponse getTodayInterview(Long interviewId) {
        Interview interview = interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        List<InterviewHint> interviewHints = interviewHintRepository.findByInterviewId(interviewId);
        List<String> keywords = interviewHints.stream().map(InterviewHint::getKeyword).toList();

        return InterviewResponse.of(interview.getId(), interview.getQuestion(), keywords);
    }

    public InterviewAnswerResponse submitInterviewAnswer(
            Long interviewId,
            InterviewAnswerRequest interviewAnswerRequest
    ) {


        return InterviewAnswerResponse.of(1L, 1L);
    }
}
