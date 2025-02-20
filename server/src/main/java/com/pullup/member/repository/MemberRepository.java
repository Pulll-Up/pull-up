package com.pullup.member.repository;

import com.pullup.auth.oAuth.domain.type.OAuthProvider;
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

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.oAuthProvider = :oAuthProvider")
    Optional<Member> findByEmailAndOAuthProvider(String email, OAuthProvider oAuthProvider);

    @Query("SELECT m.email FROM Member m WHERE m.id = :memberId")
    Optional<String> findEmailById(Long memberId);
}