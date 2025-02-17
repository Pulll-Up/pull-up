package com.pullup.exam.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.InternalServerException;
import com.pullup.common.exception.NotFoundException;
import com.pullup.common.util.IdEncryptionUtil;
import com.pullup.exam.domain.DifficultyLevel;
import com.pullup.exam.domain.Exam;
import com.pullup.exam.domain.ExamProblem;
import com.pullup.exam.dto.ExamDetailsDto;
import com.pullup.exam.dto.ExamResultDetailDto;
import com.pullup.exam.dto.ExamScoreDto;
import com.pullup.exam.dto.ExamStrengthDto;
import com.pullup.exam.dto.ProblemAndChosenAnswer;
import com.pullup.exam.dto.request.PostExamRequest;
import com.pullup.exam.dto.request.PostExamWithAnswerReqeust;
import com.pullup.exam.dto.response.GetAllExamResponse;
import com.pullup.exam.dto.response.GetExamDetailsResponse;
import com.pullup.exam.dto.response.GetExamPageResponse;
import com.pullup.exam.dto.response.GetExamResponse;
import com.pullup.exam.dto.response.GetExamResultResponse;
import com.pullup.exam.dto.response.GetExamScoresResponse;
import com.pullup.exam.dto.response.GetExamStrengthResponse;
import com.pullup.exam.dto.response.PostExamResponse;
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
import com.pullup.problem.service.ProblemAnswerService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamService {

    private static final int PROBLEM_COUNT = 10;

    private final ExamRepository examRepository;
    private final ProblemOptionRepository problemOptionRepository;
    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;
    private final ExamProblemRepository examProblemRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberExamStatisticRepository examStatisticRepository;
    private final MemberExamStatisticRepository memberExamStatisticRepository;
    private final ProblemAnswerService problemAnswerService;
    private final IdEncryptionUtil idEncryptionUtil;

    public GetExamDetailsResponse getExamDetails(Long examId) {
        Exam exam = findExamById(examId);

        List<ExamProblem> examProblems = findAllExamProblemByExamId(examId);
        List<Long> problemIds = examProblems.stream()
                .map(ep -> ep.getProblem().getId())
                .toList();

        Map<Long, Boolean> bookmarkStatusMap = getBookmarkStatusMap(problemIds, exam.getMember().getId());
        Map<Long, List<String>> problemOptionsMap = getProblemOptionsMap(problemIds);

        List<ExamDetailsDto> examDetailsDtos = examProblems.stream()
                .map(examProblem -> ExamDetailsDto.of(
                        idEncryptionUtil.encrypt(examProblem.getProblem().getId()),
                        examProblem.getProblem().getQuestion(),
                        problemOptionsMap.getOrDefault(examProblem.getProblem().getId(), Collections.emptyList()),
                        examProblem.getProblem().getSubject().name(),
                        examProblem.getProblem().getProblemType(),
                        bookmarkStatusMap.getOrDefault(examProblem.getProblem().getId(), false)
                ))
                .toList();

        return GetExamDetailsResponse.of(examDetailsDtos);
    }

    @Transactional
    public PostExamResponse postExam(PostExamRequest postExamRequest, Long memberId) {
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
        validateProblemCount(enumSubjects, correctRateRange, baseCount + 1);

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

        selectedProblems.forEach(problem -> bookmarkRepository.save(Bookmark.initBookmark(problem, member)));

        return PostExamResponse.of(idEncryptionUtil.encrypt(savedExam.getId()));
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
            Problem problem = examProblem.getProblem();

            boolean isCorrect = problemAnswerService.isCorrectAnswer(problem.getId(),
                    answer.chosenAnswer());
            examProblem.updateCheckedAnswerAndAnswerStauts(answer.chosenAnswer(), isCorrect);
            problem.updateCounts(isCorrect);

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
        List<ExamProblem> examProblems = findAllExamProblemByExamId(examId);
        List<Long> problemIds = examProblems.stream()
                .map(ep -> ep.getProblem().getId())
                .toList();
        // 북마크 여부 조회
        Map<Long, Boolean> bookmarkStatusMap = getBookmarkStatusMap(problemIds, exam.getMember().getId());
        // 시험 문제 선택옵션 조회
        Map<Long, List<String>> problemOptionsMap = getProblemOptionsMap(problemIds);

        List<ExamResultDetailDto> examResultDetailDtos = examProblems.stream()
                .map(examProblem -> ExamResultDetailDto.of(
                        idEncryptionUtil.encrypt(examProblem.getProblem().getId()),
                        examProblem.getProblem(),
                        examProblem,
                        problemOptionsMap,
                        bookmarkStatusMap
                ))
                .toList();

        return GetExamResultResponse.of(
                String.format("제 %d회 모의고사", exam.getRound()),
                exam.getScore(),
                examResultDetailDtos
        );
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

    private void validateProblemCount(
            List<Subject> subjects,
            CorrectRateRange correctRateRange,
            int minValidSubejectCount
    ) {
        for (Subject subject : subjects) {
            long problemCount = problemRepository.countBySubjectAndCorrectRateBetween(
                    subject,
                    correctRateRange.low(),
                    correctRateRange.high()
            );

            if (problemCount < minValidSubejectCount) {
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

    public GetExamResponse getExam(Long memberId) {
        Exam exam = findFirstExamByMemberId(memberId);
        List<Subject> subjects = findSubjectsOfExam(exam.getId());

        return GetExamResponse.of(idEncryptionUtil.encrypt(exam.getId()), exam, subjects);
    }

    // 시험에 있는 문제 조회해서, 문제 과목 알아오기
    private List<Subject> findSubjectsOfExam(Long examId) {
        return examProblemRepository.findByExamId(examId)
                .stream()
                .map(examProblem -> examProblem.getProblem().getSubject())
                .distinct()
                .toList();
    }

    // 멤버의 가장 최근 시험 알아오기
    private Exam findFirstExamByMemberId(Long memberId) {
        return examRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));
    }

    public GetExamPageResponse getExamPageOrderByCreatedAt(Pageable pageable, Long memberId) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );

        Page<Exam> examPage = examRepository.findAllByMemberId(memberId, sortedPageable);

        List<GetExamResponse> content = examPage.stream()
                .map(exam -> GetExamResponse.of(
                        idEncryptionUtil.encrypt(exam.getId()),
                        exam,
                        findSubjectsOfExam(exam.getId())
                ))
                .toList();

        return GetExamPageResponse.of(
                content,
                examPage.getTotalPages(),
                (int) examPage.getTotalElements(),
                examPage.isLast()
        );
    }


    public GetExamScoresResponse getRecentFiveExamScores(Long memberId) {
        List<Exam> recentExams = examRepository.findTop5ByMemberIdOrderByCreatedAtDesc(memberId);
        List<ExamScoreDto> examScoreDtos = recentExams.stream()
                .map(ExamScoreDto::of)
                .toList();
        return GetExamScoresResponse.of(examScoreDtos);
    }

    public GetAllExamResponse getAllExamOrderByCreatedAtDesc(Long memberId) {
        List<Exam> exams = examRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);

        List<GetExamResponse> examResponses = exams.stream()
                .map(exam -> GetExamResponse.of(
                        idEncryptionUtil.encrypt(exam.getId()),
                        exam,
                        findSubjectsOfExam(exam.getId()) // 시험의 과목 목록 조회
                ))
                .toList();

        return GetAllExamResponse.of(examResponses);
    }

    private List<ExamProblem> findAllExamProblemByExamId(Long examId) {
        return examProblemRepository.findAllByExamId(examId);
    }
}
