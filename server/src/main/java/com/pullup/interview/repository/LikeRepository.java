package com.pullup.interview.repository;

import com.pullup.interview.domain.Like;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {
    Boolean existsByMemberIdAndInterviewAnswerId(Long memberId, Long interviewAnswerId);

    Integer countByInterviewAnswerId(Long interviewAnswerId);
}
