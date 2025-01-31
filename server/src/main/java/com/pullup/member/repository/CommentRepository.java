package com.pullup.member.repository;

import com.pullup.interview.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Long countByInterviewAnswerId(Long interviewAnswerId);
}