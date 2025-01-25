package com.pullup.member.domain;

import com.pullup.common.auditing.BaseTimeEntity;
import com.pullup.problem.domain.Subject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "member_exam_statistic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberExamStatistic extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer wrongCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateCounts(boolean isCorrect) {
        this.totalCount++;
        if (!isCorrect) {
            this.wrongCount++;
        }
    }

    @Builder
    private MemberExamStatistic(Integer totalCount, Integer wrongCount, Subject subject, Member member) {
        this.totalCount = totalCount;
        this.wrongCount = wrongCount;
        this.subject = subject;
        this.member = member;
    }

    public static MemberExamStatistic of(Subject subject, Member member) {
        return MemberExamStatistic.builder()
                .member(member)
                .subject(subject)
                .wrongCount(0)
                .totalCount(0)
                .build();
    }


}
