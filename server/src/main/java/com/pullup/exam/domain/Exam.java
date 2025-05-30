package com.pullup.exam.domain;

import com.pullup.common.auditing.BaseTimeEntity;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.IllegalArgumentException;
import com.pullup.member.domain.Member;
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
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "exam")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Exam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Integer round;

    // 정적 팩토리 메서드 사용
    private Exam(Integer score, DifficultyLevel difficultyLevel, Member member, Integer round) {
        validateScore(score);
        validateRound(round);

        this.score = score;
        this.difficultyLevel = difficultyLevel;
        this.member = member;
        this.round = round;
    }

    public static Exam create(Integer score, DifficultyLevel difficultyLevel, Member member, Integer round) {
        return new Exam(score, difficultyLevel, member, round);
    }

    private void validateScore(Integer score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException(ErrorMessage.ERR_EXAM_SCORE_INVALID);
        }
    }

    private void validateRound(Integer round) {
        if (round < 1) {
            throw new IllegalArgumentException(ErrorMessage.ERR_EXAM_ROUND_INVALID);
        }
    }

    public void updateScore(Integer score) {
        this.score = score;
    }
}
