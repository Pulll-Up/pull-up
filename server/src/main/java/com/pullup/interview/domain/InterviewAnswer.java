package com.pullup.interview.domain;

import com.pullup.common.auditing.BaseTimeEntity;
import com.pullup.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "interview_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewAnswer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String answer;

    private String strength;

    private String weakness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;

    @Builder
    private InterviewAnswer(Member member, Interview interview, String answer) {
        this.member = member;
        this.interview = interview;
        this.answer = answer;
    }

    public static InterviewAnswer makeInterviewAnswer(Member member, Interview interview, String answer) {
        return InterviewAnswer.builder()
                .member(member)
                .interview(interview)
                .answer(answer)
                .build();
    }
}
