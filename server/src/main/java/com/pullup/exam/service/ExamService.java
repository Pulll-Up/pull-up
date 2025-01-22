package com.pullup.exam.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.exam.domain.DifficultyLevel;
import com.pullup.exam.domain.Exam;
import com.pullup.exam.domain.ExamProblem;
import com.pullup.exam.dto.ExamDetailsDto;
import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.dto.PostExamRequest;
import com.pullup.exam.repository.ExamProblemRepository;
import com.pullup.exam.repository.ExamRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.repository.MemberRepository;
import com.pullup.problem.domain.Problem;
import com.pullup.problem.domain.ProblemOption;
import com.pullup.problem.dto.CorrectRateRange;
import com.pullup.problem.repository.ProblemOptionRepository;
import com.pullup.problem.repository.ProblemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ProblemOptionRepository problemOptionRepository;
    private final ProblemRepository problemRepository;
    private final MemberRepository memberRepository;
    private final ExamProblemRepository examProblemRepository;

    public GetExamDetailsResponse getExamDetails(Long id) {
        List<ExamDetailsWithoutOptionsDto> examDetailsWithoutOptionsDtos = examRepository.findExamDetailsWithoutOptionsById(id);

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

    public Long postExam(PostExamRequest postExamRequest, Long memberId) {
        List<String> subjects = postExamRequest.subjects();
        // Enum 변환 수정
        DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(postExamRequest.difficultyLevel().toUpperCase());
        List<Problem> selectedProblems = new ArrayList<>();

        // 과목별 문제 수 계산
        int baseCount = 20 / subjects.size();
        int remainCount = 20 % subjects.size();

        // 난이도에 따른 정답률 범위 설정
        CorrectRateRange correctRateRange = getDifficultyRange(difficultyLevel);

        // 각 과목별로 문제 선택
        for (int i = 0; i < subjects.size(); i++) {
            String subject = subjects.get(i);
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

}
