package com.pullup.member.repository;

import com.pullup.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("SELECT m.solvedDays FROM Member m WHERE m.id = :memberId")
    Long findSolvedDaysById(Long memberId);
}