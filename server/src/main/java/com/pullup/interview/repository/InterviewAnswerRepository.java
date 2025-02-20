package com.pullup.interview.repository;

import com.pullup.interview.domain.Interview;
import com.pullup.interview.domain.InterviewAnswer;
import com.pullup.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long>,
        InterviewAnswerSearchRepository {

    @Query("SELECT ia FROM InterviewAnswer ia " +
            "JOIN FETCH ia.interview " +
            "WHERE ia.member.id = :memberId")
    List<InterviewAnswer> findAllByMemberIdWithInterview(Long memberId);

    @Query("SELECT ia FROM InterviewAnswer ia " +
            "JOIN FETCH ia.interview i " +
            "JOIN FETCH ia.member m " +
            "WHERE ia.interview.id = :interviewId")
    List<InterviewAnswer> findAllByInterviewIdWithMember(@Param("interviewId") Long interviewId);

    @Query("SELECT ia FROM InterviewAnswer ia " +
            "JOIN FETCH ia.interview " +
            "WHERE ia.id = :id")
    Optional<InterviewAnswer> findByIdWithInterview(Long id);

    @Query("SELECT ia FROM InterviewAnswer ia " +
            "JOIN FETCH ia.interview i " +
            "JOIN FETCH ia.member m " +
            "WHERE m.id = :memberId AND i.id = :todayInterviewId")
    Optional<InterviewAnswer> findByMemberIdAndInterviewId(Long memberId, Long todayInterviewId);

    boolean existsByMemberAndInterview(Member member, Interview interview);
}