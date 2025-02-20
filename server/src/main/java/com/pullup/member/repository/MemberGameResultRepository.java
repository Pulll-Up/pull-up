package com.pullup.member.repository;

import com.pullup.member.domain.MemberGameResult;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberGameResultRepository extends CrudRepository<MemberGameResult, Long> {

    Optional<MemberGameResult> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
