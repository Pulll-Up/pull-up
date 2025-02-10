package com.pullup.interview.domain;

import com.pullup.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "daily_quiz")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private Long memberId;

    private Long interviewId;

    @Builder
    private DailyQuiz(String question, Long memberId, Long interviewId) {
        this.question = question;
        this.memberId = memberId;
        this.interviewId = interviewId;
    }

    public static DailyQuiz createDailyQuiz(Interview interview, Member member) {
        return DailyQuiz.builder()
                .question(interview.getQuestion())
                .memberId(member.getId())
                .interviewId(interview.getId())
                .build();
    }
}
