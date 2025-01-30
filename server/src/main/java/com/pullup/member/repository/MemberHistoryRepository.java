package com.pullup.member.repository;

import com.pullup.member.domain.MemberHistory;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MemberHistoryRepository extends CrudRepository<MemberHistory, Long> {
    boolean existsByMemberId(Long memberId);

    @Query("SELECT CASE WHEN COUNT(mh) > 0 THEN "
            + "TRUE ELSE FALSE END "
            + "FROM MemberHistory mh "
            + "WHERE mh.member.id = :memberId "
            + "AND mh.isTodaySolved = TRUE "
            + "AND DATE(mh.createdAt) = :now")
    boolean existsByMemberIdAndIsTodaySolved(Long memberId, LocalDate now);
}
