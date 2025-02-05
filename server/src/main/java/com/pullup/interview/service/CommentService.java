package com.pullup.interview.service;

import com.pullup.interview.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public Integer getCommentsCount(Long interviewAnswerId) {
        return commentRepository.countByInterviewAnswerId(interviewAnswerId);
    }
}
