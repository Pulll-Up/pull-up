package com.pullup.interview.repository;

import com.pullup.interview.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Integer countByInterviewAnswerId(Long interviewAnswerId);

    @Query("SELECT c FROM Comment" +
            " c JOIN FETCH c.member" +
            " WHERE c.interviewAnswer.id = :interviewAnswerId")
    List<Comment> findByInterviewAnswerId(@Param("interviewAnswerId") Long interviewAnswerId);
}