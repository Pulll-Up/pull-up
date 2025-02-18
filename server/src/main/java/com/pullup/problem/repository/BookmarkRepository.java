package com.pullup.problem.repository;

import com.pullup.problem.domain.Bookmark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {

    // problemid, memberId로 북마크 있는지 여부 확인
    Optional<Bookmark> findByProblemIdAndMemberId(Long problemId, Long memberId);

    List<Bookmark> findAllByProblemIdInAndMemberId(List<Long> problemIds, Long memberId);

    @Query("SELECT b "
            + "FROM Bookmark b "
            + "JOIN FETCH b.problem "
            + "WHERE b.member.id = :memberId "
            + "AND b.isBookmarked = true "
            + "ORDER BY b.modifiedAt DESC")
    List<Bookmark> findBookmarkedProblemsByMemberIdWithProblemOrderByModifiedAtDesc(@Param("memberId") Long memberId);

    @Query("SELECT b FROM Bookmark b "
            + "JOIN FETCH b.problem "
            + "WHERE b.member.id = :memberId "
            + "AND b.isBookmarked = true "
            + "AND (:title IS NULL OR b.problem.question LIKE %:title%) "
            + "ORDER BY b.modifiedAt DESC")
    List<Bookmark> searchBookmarkedProblemsByMemberIdOrderByModifiedAtDesc(@Param("memberId") Long memberId,
                                                                           @Param("title") String title);

    boolean existsByProblemIdAndMemberId(Long problemId, Long memberId);
}
