package com.pullup.problem.repository;

import com.pullup.problem.domain.Bookmark;
import java.util.Optional;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {

    // problemid, memberId로 북마크 있는지 여부 확인
    Optional<Bookmark> findByProblemIdAndMemberId(Long problemId, Long memberId);

    List<Bookmark> findAllByProblemIdInAndMemberId(List<Long> problemIds, Long memberId);

}
