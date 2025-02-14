package com.pullup.exam.repository;

import com.pullup.exam.domain.Exam;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {

    Integer countByMemberId(@Param("memberId") Long memberId);

    Optional<Exam> findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);

    Page<Exam> findAllByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT e FROM Exam e WHERE e.member.id = :memberId ORDER BY e.createdAt DESC LIMIT 5")
    List<Exam> findTop5ByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId);


    List<Exam> findAllByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId);

}
