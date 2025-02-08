package com.pullup.interview.repository;

import com.pullup.interview.domain.Comment;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Integer countByInterviewAnswerId(Long interviewAnswerId);

    List<Comment> findByInterviewAnswerId(Long interviewAnswerId);
}