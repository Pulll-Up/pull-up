package com.pullup.member.repository;

import com.pullup.member.domain.InterestSubject;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InterestSubjectRepository extends CrudRepository<InterestSubject, Long> {
    List<InterestSubject> findByMemberId(Long memberId);
}
