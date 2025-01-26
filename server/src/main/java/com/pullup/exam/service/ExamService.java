package com.pullup.exam.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.InternalServerException;
import com.pullup.common.exception.NotFoundException;
import com.pullup.exam.domain.DifficultyLevel;
import com.pullup.exam.domain.Exam;
import com.pullup.exam.domain.ExamProblem;
import com.pullup.exam.dto.ExamDetailsDto;
import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.ExamResultDetailDto;
import com.pullup.exam.dto.ExamScoreDto;
import com.pullup.exam.dto.ExamStrengthDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.dto.GetExamResultResponse;
import com.pullup.exam.dto.GetExamScoresResponse;
import com.pullup.exam.dto.GetExamStrengthResponse;
import com.pullup.exam.dto.PostExamRequest;
import com.pullup.exam.dto.PostExamWithAnswerReqeust;
import com.pullup.exam.dto.ProblemAndChosenAnswer;
import com.pullup.exam.repository.ExamProblemRepository;
import com.pullup.exam.repository.ExamRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.domain.MemberExamStatistic;
import com.pullup.member.repository.MemberExamStatisticRepository;
import com.pullup.member.repository.MemberRepository;
import com.pullup.problem.domain.Bookmark;
import com.pullup.problem.domain.Problem;
import com.pullup.problem.domain.ProblemOption;
import com.pullup.problem.domain.Subject;
import com.pullup.problem.dto.CorrectRateRange;
import com.pullup.problem.repository.BookmarkRepository;
import com.pullup.problem.repository.ProblemOptionRepository;
import com.pullup.problem.repository.ProblemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamService {

    private static final int PROBLEM_COUNT = 5;

    private final ExamRepository examRepository;
    private final ProblemOptionRepository problemOptionRepository;
    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;
    private final ExamProblemRepository examProblemRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberExamStatisticRepository examStatisticRepository;
    private final MemberExamStatisticRepository memberExamStatisticRepository;

    public GetExamDetailsResponse getExamDetails(Long id) {
        List<ExamDetailsWithoutOptionsDto> examDetailsWithoutOptionsDtos = examRepository.findExamDetailsWithoutOptionsById(
                id);

        if (examDetailsWithoutOptionsDtos == null || examDetailsWithoutOptionsDtos.isEmpty()) {
            throw new NotFoundException(ErrorMessage.ERR_EXAM_NOT_FOUND);
        }

        List<ExamDetailsDto> examDetailsDtos = new ArrayList<>();
        for (ExamDetailsWithoutOptionsDto dto : examDetailsWithoutOptionsDtos) {
            List<ProblemOption> problemOptions = problemOptionRepository.findAllByProblemId(dto.problemId());
            List<String> contents = problemOptions.stream()
                    .map(ProblemOption::getContent)
                    .collect(Collectors.toList());

            examDetailsDtos.add(
                    new ExamDetailsDto(dto.problemId(), dto.problem(), contents, dto.subject().name()));
        }

        return new GetExamDetailsResponse(examDetailsDtos);

    }

    @Transactional
    public Long postExam(PostExamRequest postExamRequest, Long memberId) {
        List<String> subjects = postExamRequest.subjects();
        // 과목 리스트를 Enum으로 변환
        List<Subject> enumSubjects = subjects.stream()
                .map(subject -> Subject.valueOf(subject.toUpperCase()))
                .collect(Collectors.toList());
        // Enum 변환 수정
        DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(postExamRequest.difficultyLevel().toUpperCase());
        List<Problem> selectedProblems = new ArrayList<>();

        // 과목별 문제 수 계산
        int baseCount = PROBLEM_COUNT / subjects.size();
        int remainCount = PROBLEM_COUNT % subjects.size();

        // 난이도에 따른 정답률 범위 설정
        CorrectRateRange correctRateRange = getDifficultyRange(difficultyLevel);

        // 각 과목별로 문제 수 충분한지 먼저 확인
        validateProblemCount(enumSubjects, correctRateRange);

        // 각 과목별로 문제 선택
        for (int i = 0; i < enumSubjects.size(); i++) {
            Subject subject = enumSubjects.get(i);
            int problemCount = baseCount + (i < remainCount ? 1 : 0);

            List<Problem> problems = problemRepository.findTopNBySubjectAndCorrectRateOrderByRandom(
                    subject,
                    correctRateRange.high(),
                    correctRateRange.low(),
                    PageRequest.of(0, problemCount)
            );

            selectedProblems.addAll(problems);
        }

        // Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));

        Integer initialScore = 0;
        Integer round = examRepository.countByMemberId(memberId) + 1;

        // 시험지 생성
        Exam exam = Exam.create(
                initialScore,
                difficultyLevel,
                member,
                round
        );

        Exam savedExam = examRepository.save(exam);
        saveExamProblems(savedExam, selectedProblems);

        return savedExam.getId();
    }

    @Transactional
    public void postExamWithAnswer(Long examId, PostExamWithAnswerReqeust request, Long memberId) {
        // 시험 존재 여부 확인
        Exam exam = findExamById(examId);
        // 시험 문제 mapping
        Map<Long, ExamProblem> examProblemMap = getExamProblemMap(examId);

        // 각 답안 처리
        for (ProblemAndChosenAnswer answer : request.problemAndChosenAnswers()) {
            ExamProblem examProblem = examProblemMap.get(answer.problemId());
            if (examProblem == null) {
                throw new NotFoundException(ErrorMessage.ERR_EXAM_PROBLEM_NOT_FOUND);
            }

            examProblem.updateCheckedAnswerAndAnswerStauts(answer.chosenAnswer());

            MemberExamStatistic memberExamStatistic = memberExamStatisticRepository.findByMemberIdAndSubject(memberId,
                            examProblem.getProblem().getSubject())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_EXAM_STATISTIC_NOT_FOUND));

            // total count, wrong count 업데이트하기
            boolean isAnswerCorrect = examProblem.getAnswerStatus();
            memberExamStatistic.updateCounts(isAnswerCorrect);


        }

        // 시험 전체 점수 계산 및 업데이트
        updateExamScore(exam, new ArrayList<>(examProblemMap.values()));
    }

    public GetExamResultResponse getExamResult(Long examId) {
        // 시험 조회
        Exam exam = findExamById(examId);
        // 시험 문제 조회
        List<ExamProblem> examProblems = examProblemRepository.findAllByExamId(examId);
        List<Long> problemIds = examProblems.stream()
                .map(ep -> ep.getProblem().getId())
                .toList();
        // 북마크 여부 조회
        Map<Long, Boolean> bookmarkStatusMap = getBookmarkStatusMap(problemIds, exam.getMember().getId());
        // 시험 문제 선택옵션 조회
        Map<Long, List<String>> problemOptionsMap = getProblemOptionsMap(problemIds);

        List<ExamResultDetailDto> examResultDetailDtos = examProblems.stream()
                .map(examProblem -> ExamResultDetailDto.of(
                        examProblem,
                        problemOptionsMap,
                        bookmarkStatusMap,
                        exam.getRound()))
                .toList();

        return new GetExamResultResponse(examResultDetailDtos);
    }

    public GetExamStrengthResponse getExamStrength(Long memberId) {
        List<MemberExamStatistic> statistics = examStatisticRepository.findAllByMemberId(memberId);

        List<ExamStrengthDto> strengthDtos = statistics.stream()
                .map(stat -> new ExamStrengthDto(
                        stat.getSubject().name(),
                        stat.calculateCorrectRate(stat.getTotalCount(), stat.getWrongCount())
                ))
                .collect(Collectors.toList());

        return new GetExamStrengthResponse(strengthDtos);
    }

    private Map<Long, Boolean> getBookmarkStatusMap(List<Long> problemIds, Long memberId) {
        return bookmarkRepository.findAllByProblemIdInAndMemberId(problemIds, memberId)
                .stream()
                .collect(Collectors.toMap(
                        bookmark -> bookmark.getProblem().getId(),
                        Bookmark::getIsBookmarked
                ));
    }

    private Map<Long, List<String>> getProblemOptionsMap(List<Long> problemIds) {
        return problemOptionRepository.findAllByProblemIdIn(problemIds)
                .stream()
                .collect(Collectors.groupingBy(
                        option -> option.getProblem().getId(),
                        Collectors.mapping(ProblemOption::getContent, Collectors.toList())
                ));
    }


    private void updateExamScore(Exam exam, List<ExamProblem> examProblems) {
        long correctCount = examProblems.stream()
                .filter(ExamProblem::getAnswerStatus)
                .count();

        int totalScore = calculateScore(correctCount, examProblems.size());
        exam.updateScore(totalScore);
    }

    private int calculateScore(long correctCount, int totalProblems) {
        return (int) ((correctCount * 100.0) / totalProblems);
    }


    private CorrectRateRange getDifficultyRange(DifficultyLevel difficultyLevel) {
        return switch (difficultyLevel) {
            case HARD -> new CorrectRateRange(0, 29);
            case MEDIUM -> new CorrectRateRange(30, 69);
            case EASY -> new CorrectRateRange(70, 100);  // LOW를 EASY로 수정
        };
    }

    private void saveExamProblems(Exam exam, List<Problem> problems) {
        List<ExamProblem> examProblems = problems.stream()
                .map(problem -> ExamProblem.create(exam, problem))
                .collect(Collectors.toList());

        examProblemRepository.saveAll(examProblems);
    }

    private void validateProblemCount(List<Subject> subjects, CorrectRateRange correctRateRange) {
        for (Subject subject : subjects) {
            long problemCount = problemRepository.countBySubjectAndCorrectRateBetween(
                    subject,
                    correctRateRange.low(),
                    correctRateRange.high()
            );

            if (problemCount < PROBLEM_COUNT) {
                throw new InternalServerException(
                        ErrorMessage.ERR_INSUFFICIENT_PROBLEMS
                );
            }
        }
    }

    private Map<Long, ExamProblem> getExamProblemMap(Long examId) {
        List<ExamProblem> examProblems = examProblemRepository.findAllByExamId(examId);
        return examProblems.stream()
                .collect(Collectors.toMap(
                        ep -> ep.getProblem().getId(),
                        ep -> ep
                ));
    }

    private Exam findExamById(Long examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_EXAM_NOT_FOUND));
    }


    public GetExamScoresResponse getRecentFiveExamScores(Long memberId) {
        List<Exam> recentExams = examRepository.findTop5ByMemberIdOrderByCreatedAtDesc(memberId);
        List<ExamScoreDto> examScoreDtos = recentExams.stream()
                .map(ExamScoreDto::of)
                .toList();
        return GetExamScoresResponse.of(examScoreDtos);
    }
}
