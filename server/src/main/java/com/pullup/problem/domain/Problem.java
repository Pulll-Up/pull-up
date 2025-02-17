package com.pullup.problem.domain;

import com.pullup.common.auditing.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "problem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String explanation;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer correctCount;

    @Column(nullable = false)
    private Integer correctRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemType problemType;

    public void updateCounts(boolean isCorrect) {
        this.totalCount++;
        if (isCorrect) {
            this.correctCount++;
        }
        calculateCorrectRate();
    }

    private void calculateCorrectRate() {
        if (this.totalCount > 0) {
            this.correctRate = (this.correctCount * 100) / this.totalCount;
        } else {
            this.correctRate = 0;
        }
    }

}
