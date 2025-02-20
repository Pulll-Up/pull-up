package com.pullup.problem.repository;

import com.pullup.problem.domain.ProblemAnswer;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ProblemAnswerRepository extends CrudRepository<ProblemAnswer, Long> {
    List<ProblemAnswer> findByProblemId(Long problemId);
}
