package com.pullup.interview.repository;

import com.pullup.interview.domain.Like;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {

    List<Like> findByInterviewAnswerId(Long interviewAnswerId);

    Optional<Like> findByMemberIdAndInterviewAnswerId(Long memberId, Long interviewAnswerId);
}