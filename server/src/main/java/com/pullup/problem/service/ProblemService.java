package com.pullup.problem.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.exam.domain.ExamProblem;
import com.pullup.exam.repository.ExamProblemRepository;
import com.pullup.game.dto.CardType;
import com.pullup.game.dto.ProblemCard;
import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.repository.MemberRepository;
import com.pullup.problem.domain.Bookmark;
import com.pullup.problem.domain.Problem;
import com.pullup.problem.domain.Subject;
import com.pullup.problem.dto.BookmarkedProblemDto;
import com.pullup.problem.dto.RecentWrongQuestionDto;
import com.pullup.problem.dto.WrongProblemDto;
import com.pullup.problem.dto.response.GetAllWrongProblemsResponse;
import com.pullup.problem.dto.response.GetBookmarkedProblemsResponse;
import com.pullup.problem.dto.response.GetProblemResponse;
import com.pullup.problem.dto.response.GetRecentWrongProblemsResponse;
import com.pullup.problem.dto.response.SearchBookmarkedProblemsResponse;
import com.pullup.problem.dto.response.SearchWrongProblemsResponse;
import com.pullup.problem.repository.BookmarkRepository;
import com.pullup.problem.repository.ProblemOptionRepository;
import com.pullup.problem.repository.ProblemRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private static final int NUMBER_OF_PROBLEMS = 8;

    private final ProblemRepository problemRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ProblemOptionRepository problemOptionRepository;
    private final ExamProblemRepository examProblemRepository;
    private final GameRoomRepository gameRoomRepository;

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

    public Problem findProblemById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_PROBLEM_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));
    }


    public GetBookmarkedProblemsResponse getBookmarkedProblems(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarkedProblemsByMemberIdWithProblemOrderByModifiedAtDesc(
                memberId);
        List<BookmarkedProblemDto> bookmarkedProblemDtos = bookmarks.stream()
                .map(BookmarkedProblemDto::of)
                .toList();

        return GetBookmarkedProblemsResponse.of(bookmarkedProblemDtos);
    }

    public GetProblemResponse getProblem(Long problemId, Long memberId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_PROBLEM_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByProblemIdAndMemberId(problemId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_BOOKMARK_NOT_FOUND));

        List<String> options = problemOptionRepository.findAllByProblemId(problemId)
                .stream()
                .map(problemOption -> problemOption.getContent())
                .collect(Collectors.toList());

        return GetProblemResponse.of(problem, options, bookmark);
    }

    public GetRecentWrongProblemsResponse getRecentWrongProblems(Long memberId) {
        List<RecentWrongQuestionDto> wrongProblems = examProblemRepository.findTop10ByExamMemberIdAndAnswerStatusOrderByCreatedAtDesc(
                        memberId, false)
                .stream()
                .map(examProblem -> new RecentWrongQuestionDto(
                        examProblem.getProblem().getId(),
                        examProblem.getProblem().getQuestion(),
                        examProblem.getProblem().getSubject()
                ))
                .collect(Collectors.toMap(
                        RecentWrongQuestionDto::problemId, // 중복 제거 기준 (problemId)
                        dto -> dto,  // 그대로 유지
                        (existing, replacement) -> existing  // 중복 발생 시 기존 값 유지
                ))
                .values()
                .stream()
                .collect(Collectors.toList()); // 중복 제거 후 리스트 변환

        return new GetRecentWrongProblemsResponse(wrongProblems);
    }


    public GetAllWrongProblemsResponse getAllWrongProblem(Long memberId) {
        List<WrongProblemDto> wrongProblemDtos = examProblemRepository.findByExamMemberIdAndAnswerStatusFalseOrderByCreatedAtDesc(
                        memberId)
                .stream()
                .map(examProblem -> WrongProblemDto.of(
                        examProblem.getProblem().getId(),
                        examProblem.getProblem().getQuestion(),
                        examProblem.getProblem().getSubject(),
                        examProblem.getCreatedAt()
                ))
                .collect(Collectors.toMap(
                        WrongProblemDto::problemId,  // 중복 제거 기준 (problemId)
                        dto -> dto,                  // 그대로 유지
                        (existing, replacement) -> existing  // 중복 발생 시 기존 값 유지
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(WrongProblemDto::date).reversed()) // 최신순 정렬
                .collect(Collectors.toList());

        return GetAllWrongProblemsResponse.of(wrongProblemDtos);
    }


    public void generateProblems(String roomId, CreateRoomWithSubjectsRequest request) {
        List<Subject> selectedSubjects = getSelectedSubjects(request);
        int numSubjects = selectedSubjects.size();
        int numQuestionsPerSubject = NUMBER_OF_PROBLEMS / numSubjects; // 각 과목별 기본 문제 개수
        int remainingQuestions = NUMBER_OF_PROBLEMS % numSubjects;     // 나누어떨어지지 않을 때 추가해야 하는 문제 개수

        List<ProblemCard> finalProblems = new ArrayList<>();
        Set<Long> selectedProblemIds = new HashSet<>(); // 중복 방지를 위한 ID 저장

        // 각 과목별 기본 개수만큼 문제 가져오기
        for (Subject subject : selectedSubjects) {
            List<Problem> problems = problemRepository.findRandomShortAnswerProblemsBySubject(subject.name(),
                    numQuestionsPerSubject);

            for (Problem problem : problems) {
                if (selectedProblemIds.add(problem.getId())) { // 중복되지 않는 경우에만 추가
                    finalProblems.add(
                            ProblemCard.createNewProblemCard(
                                    problem.getId(),
                                    CardType.QUESTION,
                                    problem.getQuestion()));
                    finalProblems.add(
                            ProblemCard.createNewProblemCard(
                                    problem.getId(),
                                    CardType.ANSWER,
                                    problem.getAnswer()));
                }
            }
        }

        // 남은 문제 수 만큼 랜덤한 과목에서 한 번에 가져오기
        Collections.shuffle(selectedSubjects); // 랜덤 과목 순서 섞기
        int remainingToFetch = remainingQuestions; // 남은 문제 개수

        for (Subject subject : selectedSubjects) {
            if (remainingToFetch <= 0) {
                break; // 남은 문제가 없으면 종료
            }

            List<Problem> extraProblems = problemRepository.findRandomShortAnswerProblemsBySubject(subject.name(),
                    remainingToFetch);

            for (Problem problem : extraProblems) {
                if (selectedProblemIds.add(problem.getId())) { // 중복 방지
                    finalProblems.add(
                            ProblemCard.createNewProblemCard(
                                    problem.getId(),
                                    CardType.QUESTION,
                                    problem.getQuestion()));
                    finalProblems.add(
                            ProblemCard.createNewProblemCard(
                                    problem.getId(),
                                    CardType.ANSWER,
                                    problem.getAnswer()));
                    remainingToFetch--; // 남은 문제 개수 감소
                    if (remainingToFetch <= 0) {
                        break;
                    }
                }
            }

        }
        // 문제 섞기
        Collections.shuffle(finalProblems);
        gameRoomRepository.saveProblems(roomId, finalProblems);
    }

    public void generateProblemsForRandomMatching(String roomId) {
        List<ProblemCard> finalProblems = new ArrayList<>();

        List<Problem> randomProblems = problemRepository.findRandomShortAnswerProblems(NUMBER_OF_PROBLEMS);
        for (Problem problem : randomProblems) {
            finalProblems.add(
                    ProblemCard.createNewProblemCard(
                            problem.getId(),
                            CardType.QUESTION,
                            problem.getQuestion()));
            finalProblems.add(
                    ProblemCard.createNewProblemCard(
                            problem.getId(),
                            CardType.ANSWER,
                            problem.getAnswer()));

        }

        gameRoomRepository.saveProblems(roomId, finalProblems);
    }

    private List<Subject> getSelectedSubjects(CreateRoomWithSubjectsRequest request) {
        List<Subject> selectedSubjects = new ArrayList<>();

        if (request.algorithm()) {
            selectedSubjects.add(Subject.ALGORITHM);
        }
        if (request.computerArchitecture()) {
            selectedSubjects.add(Subject.COMPUTER_ARCHITECTURE);
        }
        if (request.database()) {
            selectedSubjects.add(Subject.DATABASE);
        }
        if (request.dataStructure()) {
            selectedSubjects.add(Subject.DATA_STRUCTURE);
        }
        if (request.network()) {
            selectedSubjects.add(Subject.NETWORK);
        }
        if (request.operatingSystem()) {
            selectedSubjects.add(Subject.OPERATING_SYSTEM);
        }

        return selectedSubjects;
    }

    public SearchWrongProblemsResponse searchWrongProblemsByTitle(Long memberId, String title) {
        List<ExamProblem> examProblems = examProblemRepository.searchByMemberIdAndAnswerStatusFalseOrderByCreatedAtDesc(
                memberId, title);
        // 문제 ID 기준으로 중복 제거
        Map<Long, WrongProblemDto> uniqueProblemsMap = examProblems.stream()
                .collect(Collectors.toMap(
                        ep -> ep.getProblem().getId(),
                        ep -> WrongProblemDto.of(
                                ep.getProblem().getId(),
                                ep.getProblem().getQuestion(),
                                ep.getProblem().getSubject(),
                                ep.getCreatedAt()
                        ),
                        (existing, replacement) -> existing
                ));

        List<WrongProblemDto> uniqueWrongProblems = List.copyOf(uniqueProblemsMap.values());

        return SearchWrongProblemsResponse.of(uniqueWrongProblems);
    }

    public SearchBookmarkedProblemsResponse searchBookmarkedProblemsByTitle(Long memberId, String title) {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmarkedProblemsByMemberIdOrderByModifiedAtDesc(memberId,
                title);

        List<BookmarkedProblemDto> bookmarkedProblemDtos = bookmarks.stream()
                .map(BookmarkedProblemDto::of)
                .collect(Collectors.toList());

        return SearchBookmarkedProblemsResponse.of(bookmarkedProblemDtos);

    }

}
