package com.poolup.poolup.shared.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.exam.domain.Exam;
import com.poolup.poolup.problem.domain.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExamProblem extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long memberCheckedAnswer;
    private boolean bookmark;

    @ManyToOne @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne @JoinColumn(name = "problem_id")
    private Problem problem;


}
