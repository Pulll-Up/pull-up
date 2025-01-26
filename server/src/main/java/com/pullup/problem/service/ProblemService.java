package com.pullup.problem.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.member.domain.Member;
import com.pullup.member.repository.MemberRepository;
import com.pullup.problem.domain.Bookmark;
import com.pullup.problem.domain.Problem;
import com.pullup.problem.dto.GetProblemResponse;
import com.pullup.problem.repository.BookmarkRepository;
import com.pullup.problem.repository.ProblemOptionRepository;
import com.pullup.problem.repository.ProblemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ProblemOptionRepository problemOptionRepository;

    @Transactional
    public void toggleProblemBookmark(Long problemId, Long memberId) {
        Problem problem = findProblemById(problemId);
        Member member = findMemberById(memberId);

        bookmarkRepository.findByProblemIdAndMemberId(problemId, memberId)
                .ifPresentOrElse(
                        bookmark -> bookmark.toggleBookmarked(),
                        () -> {
                            Bookmark newBookmark = Bookmark.builder()
                                    .problem(problem)
                                    .member(member)
                                    .isBookmarked(true)
                                    .build();
                            bookmarkRepository.save(newBookmark);
                        }
                );

    }

    private Problem findProblemById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_PROBLEM_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));
    }


    public GetProblemResponse getProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_PROBLEM_NOT_FOUND));

        List<String> options = problemOptionRepository.findAllByProblemId(problemId)
                .stream()
                .map(problemOption -> problemOption.getContent())
                .collect(Collectors.toList());

        return GetProblemResponse.of(problem, options);
    }
}
