package com.pullup.interview.service;

import static com.pullup.interview.domain.DailyQuiz.createDailyQuiz;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.interview.domain.DailyQuiz;
import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.interview.domain.InterviewHint;
import com.pullup.interview.dto.InterviewAnswerDto;
import com.pullup.interview.dto.MyInterviewAnswerDto;
import com.pullup.interview.dto.request.MyInterviewAnswerRequest;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.InterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResultResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.repository.DailyQuizRepository;
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
    private final DailyQuizRepository dailyQuizRepository;

    @Transactional
    public InterviewResponse getTodayInterview(Long memberId) {
        return dailyQuizRepository.findInterviewIdByMemberId(memberId)
                .map(this::getInterviewResponse)
                .orElseGet(() -> createNewDailyQuiz(memberId));
    }

    private InterviewResponse getInterviewResponse(Long interviewId) {
        Interview interview = interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        List<String> keywords = getKeywords(interviewId);

        return InterviewResponse.of(interview.getId(), interview.getQuestion(), keywords);
    }

    private InterviewResponse createNewDailyQuiz(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        Interview interview = getRandomUnansweredInterview(memberId);
        saveDailyQuiz(member, interview);

        List<String> keywords = getKeywords(interview.getId());
        return InterviewResponse.of(interview.getId(), interview.getQuestion(), keywords);
    }

    public Interview getRandomUnansweredInterview(Long memberId) {
        //TODO : 관심 주제를 모두 입력 받은 후에는, 해당 로직 분기 처리 수정 필요
        return interviewRepository.findRandomUnansweredInterview(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));
    }

    public void saveDailyQuiz(Member member, Interview interview) {
        dailyQuizRepository.save(createDailyQuiz(interview, member));
    }

    @Transactional
    public void saveAllDailyQuiz(List<DailyQuiz> dailyQuizzes) {
        dailyQuizRepository.saveAll(dailyQuizzes);
    }

    @Transactional
    public MyInterviewAnswerResponse submitInterviewAnswer(
            Long memberId,
            Long interviewId,
            MyInterviewAnswerRequest myInterviewAnswerRequest
    ) {
        Member member = memberService.findMemberById(memberId);

        Interview interview = interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        //TODO : GPT API 연동 후, 강점, 약점 분석 로직 추가

        memberService.updateSolveStatus(member);

        InterviewAnswer interviewAnswer = interviewAnswerRepository.save(InterviewAnswer.createInterviewAnswer(
                member,
                interview,
                myInterviewAnswerRequest.answer()
        ));

        return MyInterviewAnswerResponse.of(interviewId, interviewAnswer.getId());
    }

    public MyInterviewAnswerResultResponse getMyInterviewAnswerResult(Long interviewAnswerId) {
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findByIdWithInterview(interviewAnswerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND));

        return MyInterviewAnswerResultResponse.of(
                interviewAnswer.getInterview().getId(),
                interviewAnswerId,
                interviewAnswer.getInterview().getQuestion(),
                interviewAnswer.getAnswer(),
                getKeywords(interviewAnswer.getInterview().getId()),
                interviewAnswer.getCreatedAt(),
                interviewAnswer.getStrength(),
                interviewAnswer.getWeakness(),
                interviewAnswer.getInterview().getAnswer()
        );
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

    public InterviewAnswerResponse getInterviewAnswer(Long memberId, Long interviewAnswerId) {
        InterviewAnswer interviewAnswer = interviewAnswerRepository.findById(interviewAnswerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND));

        List<String> keywords = getKeywords(interviewAnswer.getInterview().getId());

        InterviewAnswerDto interviewAnswerDto = makeInterviewAnswerDto(
                interviewAnswer,
                keywords,
                memberId
        );

        return InterviewAnswerResponse.of(interviewAnswerDto);
    }

    public InterviewAnswersResponse getInterviewAnswers(Long memberId, Long interviewId) {
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.findAllByInterviewId(interviewId);

        if (interviewAnswers.isEmpty()) {
            throw new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND);
        }

        List<String> keywords = getKeywords(interviewId);

        List<InterviewAnswerDto> interviewAnswerDtos = interviewAnswers.stream()
                .map(interviewAnswer -> makeInterviewAnswerDto(
                        interviewAnswer,
                        keywords,
                        memberId
                ))
                .toList();

        return InterviewAnswersResponse.of(interviewAnswerDtos);
    }

    private InterviewAnswerDto makeInterviewAnswerDto(InterviewAnswer interviewAnswer, List<String> keywords,
                                                      Long memberId) {
        return InterviewAnswerDto.of(
                interviewAnswer.getId(),
                interviewAnswer.getInterview().getQuestion(),
                keywords,
                interviewAnswer.getMember().getName(),
                interviewAnswer.getAnswer(),
                interviewAnswer.getCreatedAt(),
                likeService.isLikedInterviewAnswerByMember(memberId, interviewAnswer.getId()),
                likeService.getLikesCount(interviewAnswer.getId()),
                commentService.getCommentsCount(interviewAnswer.getId())
        );
    }

    private List<String> getKeywords(Long interviewId) {
        List<InterviewHint> interviewHints = interviewHintRepository.findByInterviewId(interviewId);
        return interviewHints.stream().map(InterviewHint::getKeyword).toList();
    }
}
