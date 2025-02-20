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
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean isLiked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_answer_id", nullable = false)
    private InterviewAnswer interviewAnswer;

    @Builder
    private Like(Member member, InterviewAnswer interviewAnswer) {
        this.member = member;
        this.interviewAnswer = interviewAnswer;
        this.isLiked = true;
    }

    public static Like createLike(Member member, InterviewAnswer interviewAnswer) {
        return Like.builder()
                .member(member)
                .interviewAnswer(interviewAnswer)
                .build();
    }

    public void toggleLike() {
        this.isLiked = !this.isLiked;
    }

    public Boolean isLiked() {
        return isLiked;
    }
}
