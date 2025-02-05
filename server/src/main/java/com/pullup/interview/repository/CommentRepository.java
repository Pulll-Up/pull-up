package com.pullup.interview.repository;

import com.pullup.interview.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Integer countByInterviewAnswerId(Long interviewAnswerId);
}