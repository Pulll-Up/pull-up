package com.pullup.member.repository;

import com.pullup.interview.domain.Like;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {
    Boolean existsByMemberIdAndInterviewAnswerId(Long memberId, Long interviewAnswerId);
    Long countByInterviewAnswerId(Long interviewAnswerId);
}
