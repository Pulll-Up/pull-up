package com.pullup.problem.service;

import com.pullup.problem.domain.Problem;
import com.pullup.problem.domain.ProblemAnswer;
import com.pullup.problem.repository.ProblemAnswerRepository;
import com.pullup.problem.util.AnswerUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemAnswerService {

    private final ProblemAnswerRepository problemAnswerRepository;
    private final ProblemService problemService;

    public boolean isCorrectAnswer(Long problemId, String userAnswer) {
        // 문제 테이블에서 기본 정답 가져오기
        Problem problem = problemService.findProblemById(problemId);

        // 사용자가 입력한 답변 전처리
        String normalizedUserAnswer = AnswerUtil.normalizeAnswer(userAnswer);
        String normalizedCorrectAnswer = AnswerUtil.normalizeAnswer(problem.getAnswer());

        // 문제 테이블의 기본 정답과 먼저 비교
        if (normalizedUserAnswer.equals(normalizedCorrectAnswer)) {
            return true; // 기본 정답이 맞으면 추가 비교 없이 정답 처리
        }

        // 추가적인 정답 리스트 가져오기
        List<ProblemAnswer> correctProblemAnswers = problemAnswerRepository.findByProblemId(problemId);

        // 추가 정답 비교
        return correctProblemAnswers.stream()
                .map(answer -> AnswerUtil.normalizeAnswer(answer.getAnswer()))
                .anyMatch(normalizedAnswer -> normalizedAnswer.equals(normalizedUserAnswer));
    }
}
