package com.pullup.problem.repository;

import com.pullup.problem.domain.ProblemOption;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemOptionRepository extends CrudRepository<ProblemOption, Long> {
    List<ProblemOption> findAllByProblemId(Long problemId);

    List<ProblemOption> findAllByProblemIdIn(List<Long> problemIds);
}
