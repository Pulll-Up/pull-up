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
import com.pullup.interview.dto.SearchedInterviewQuestionDto;
import com.pullup.interview.dto.response.InterviewAnswerResponse;
import com.pullup.interview.dto.response.InterviewAnswersResponse;
import com.pullup.interview.dto.response.InterviewResponse;
import com.pullup.interview.dto.response.MyInterviewAnswerResultResponse;
import com.pullup.interview.dto.response.MyInterviewAnswersResponse;
import com.pullup.interview.dto.response.SearchedInterviewQuestionsResponse;
import com.pullup.interview.repository.DailyQuizRepository;
import com.pullup.interview.repository.InterviewAnswerRepository;
import com.pullup.interview.repository.InterviewHintRepository;
import com.pullup.interview.repository.InterviewRepository;
import com.pullup.member.domain.Member;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterviewService {

    private final LikeService likeService;
    private final CommentService commentService;
    private final InterviewRepository interviewRepository;
    private final InterviewHintRepository interviewHintRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;
    private final DailyQuizRepository dailyQuizRepository;

    public Interview findInterviewById(Long interviewId) {
        return interviewRepository.findInterviewById(interviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));
    }

    public InterviewAnswer saveInterviewAnswer(Member member, Interview interview, String answer) {
        return interviewAnswerRepository.save(InterviewAnswer.createInterviewAnswer(
                member,
                interview,
                answer
        ));
    }

    public Long getTodayInterviewAnswerId(Long memberId) {
        Long todayInterviewId = dailyQuizRepository.findInterviewIdByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));

        InterviewAnswer interviewAnswer = interviewAnswerRepository.findByMemberIdAndInterviewId(memberId,
                        todayInterviewId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND));

        return interviewAnswer.getId();
    }

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
        Interview interview = getRandomInterview(memberId);
        saveDailyQuiz(interview.getQuestion(), memberId, interview.getId());

        List<String> keywords = getKeywords(interview.getId());
        return InterviewResponse.of(interview.getId(), interview.getQuestion(), keywords);
    }

    public Interview getRandomInterview(Long memberId) {
        return getUnansweredInterviewByRandomAndSubject(memberId)
                .orElseGet(() -> getUnansweredInterviewByRandom(memberId));
    }

    public Optional<Interview> getUnansweredInterviewByRandomAndSubject(Long memberId) {
        return interviewRepository.findUnansweredInterviewByRandomAndSubject(memberId);
    }

    public Interview getUnansweredInterviewByRandom(Long memberId) {
        return interviewRepository.findUnansweredInterviewByRandom(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_NOT_FOUND));
    }

    public void saveDailyQuiz(String question, Long memberId, Long interviewId) {
        dailyQuizRepository.save(createDailyQuiz(question, memberId, interviewId));
    }

    @Transactional
    public void saveAllDailyQuiz(List<DailyQuiz> dailyQuizzes) {
        dailyQuizRepository.saveAll(dailyQuizzes);
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
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.findAllByMemberIdWithInterview(memberId);

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
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.findAllByInterviewIdWithMember(interviewId);

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
                .sorted(Comparator.comparingLong(InterviewAnswerDto::likeCount).reversed())
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

    public SearchedInterviewQuestionsResponse getSearchedInterviewQuestions(Long memberId, String keyword) {
        List<InterviewAnswer> interviewAnswers = interviewAnswerRepository.searchByKeyword(memberId, keyword);

        return SearchedInterviewQuestionsResponse.of((interviewAnswers.stream()
                .map(interviewAnswer -> SearchedInterviewQuestionDto.of(
                        interviewAnswer.getId(),
                        interviewAnswer.getInterview().getQuestion()
                )).toList()
        ));
    }

    private InterviewAnswer findInterviewAnswerById(Long interviewAnswerId) {
        return interviewAnswerRepository.findById(interviewAnswerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_INTERVIEW_ANSWER_NOT_FOUND));
    }
}
