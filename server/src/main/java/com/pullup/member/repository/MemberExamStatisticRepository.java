package com.pullup.member.repository;

import com.pullup.member.domain.MemberExamStatistic;
import com.pullup.problem.domain.Subject;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberExamStatisticRepository extends CrudRepository<MemberExamStatistic, Long> {
    List<MemberExamStatistic> findAllByMemberId(Long memberId);

    Optional<MemberExamStatistic> findByMemberIdAndSubject(Long memberId, Subject subject);

    Boolean existsByMemberId(Long memberId);
}
