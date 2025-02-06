package com.pullup.member.repository;

import com.pullup.member.domain.MemberGameResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberGameResultRepository extends CrudRepository<MemberGameResult, Long> {
    List<MemberGameResult> findByMemberId(Long memberId);
}
