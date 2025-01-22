package com.pullup.exam.domain;

import com.pullup.common.auditing.BaseTimeEntity;
import com.pullup.problem.domain.Problem;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "exam_problem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamProblem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberCheckedAnswer;

    @Column(nullable = false)
    private Boolean answerStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    private ExamProblem(Exam exam, Problem problem) {
        this.exam = exam;
        this.problem = problem;
        this.memberCheckedAnswer = "";  // 초기에는 빈 값
        this.answerStatus = false;      // 초기에는 미체크 상태
    }

    public static ExamProblem create(Exam exam, Problem problem) {
        return new ExamProblem(exam, problem);
    }

    public void updateCheckedAnswerAndAnswerStauts(String checkedAnswer) {
        this.memberCheckedAnswer = checkedAnswer;
        this.answerStatus = this.problem.getAnswer().equals(checkedAnswer);
    }




}
