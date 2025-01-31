package com.pullup.member.service;

import com.pullup.member.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    public Boolean isLikedInterviewAnswerByMember(Long memberId, Long interviewAnswerId) {
        return likeRepository.existsByMemberIdAndInterviewAnswerId(memberId, interviewAnswerId);
    }

    public Integer getLikesCount(Long interviewAnswerId) {
        return likeRepository.countByInterviewAnswerId(interviewAnswerId);
    }
}