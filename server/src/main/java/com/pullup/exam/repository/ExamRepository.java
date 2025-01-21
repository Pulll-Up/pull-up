package com.pullup.exam.repository;

import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.domain.Exam;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {

    @Query("SELECT new com.pullup.exam.dto.ExamDetailsWithoutOptionsDto("
            + "p.id, p.question, p.subject) " +
            "FROM Exam e " +
            "JOIN ExamProblem ep ON e.id = ep.exam.id " +
            "JOIN Problem p ON ep.problem.id = p.id " +
            "WHERE e.id = :examId")
    List<ExamDetailsWithoutOptionsDto> findExamDetailsWithoutOptionsById(@Param("examId") Long examId);
}
