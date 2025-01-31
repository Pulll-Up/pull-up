package com.pullup.member.service;

import com.pullup.member.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Long getCommentsCount(Long interviewAnswerId) {
        return commentRepository.countByInterviewAnswerId(interviewAnswerId);
    }
}
