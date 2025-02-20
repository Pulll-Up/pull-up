package com.pullup.member.repository;

import com.pullup.member.domain.InterestSubject;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InterestSubjectRepository extends CrudRepository<InterestSubject, Long> {
    List<InterestSubject> findByMemberId(Long memberId);

    Boolean existsByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM InterestSubject i WHERE i.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);
}