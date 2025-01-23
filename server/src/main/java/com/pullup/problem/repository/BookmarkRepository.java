package com.pullup.problem.repository;

import com.pullup.problem.domain.Bookmark;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
    List<Bookmark> findAllByProblemIdInAndMemberId(List<Long> problemIds, Long memberId);
}
