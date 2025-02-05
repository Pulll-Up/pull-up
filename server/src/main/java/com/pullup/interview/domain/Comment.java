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
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_answer_id", nullable = false)
    private InterviewAnswer interviewAnswer;

    @Builder
    public Comment(String content, Member member, InterviewAnswer interviewAnswer) {
        this.content = content;
        this.member = member;
        this.interviewAnswer = interviewAnswer;
    }

    public static Comment createComment(String content, Member member, InterviewAnswer interviewAnswer) {
        return Comment.builder()
                .content(content)
                .member(member)
                .interviewAnswer(interviewAnswer)
                .build();
    }

    public void modifyContent(String content) {
        this.content = content;
    }
}
