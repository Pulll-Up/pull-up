package com.pullup.member.service;

import com.pullup.member.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Boolean isLikedInterviewAnswerByMember(Long memberId, Long interviewAnswerId) {
        return likeRepository.existsByMemberIdAndInterviewAnswerId(memberId, interviewAnswerId);
    }

    public Long getLikesCount(Long interviewAnswerId) {
        return likeRepository.countByInterviewAnswerId(interviewAnswerId);
    }
}
