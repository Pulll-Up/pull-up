package com.pullup.member.repository;

import com.pullup.member.domain.MemberExamStatistic;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberExamStatisticRepository extends CrudRepository<MemberExamStatistic, Long> {
    List<MemberExamStatistic> findAllByMemberId(Long memberId);
}
