package com.pullup.interview.repository;

import com.pullup.interview.domain.Like;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {

    Integer countByInterviewAnswerId(Long interviewAnswerId);

    Optional<Like> findByMemberIdAndInterviewAnswerId(Long memberId, Long interviewAnswerId);
}