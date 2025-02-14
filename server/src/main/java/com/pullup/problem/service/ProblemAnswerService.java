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
        Problem problem = problemService.findProblemById(problemId);

        // 기본 정답 비교
        String normalizedUserAnswer = AnswerUtil.normalizeAnswer(userAnswer);
        String normalizedCorrectAnswer = AnswerUtil.normalizeAnswer(problem.getAnswer());
        if (normalizedUserAnswer.equals(normalizedCorrectAnswer)) {
            return true;
        }

        // 추가 정답 비교
        List<ProblemAnswer> correctProblemAnswers = problemAnswerRepository.findByProblemId(problemId);
        return correctProblemAnswers.stream()
                .map(answer -> AnswerUtil.normalizeAnswer(answer.getAnswer()))
                .anyMatch(normalizedAnswer -> normalizedAnswer.equals(normalizedUserAnswer));
    }
}
