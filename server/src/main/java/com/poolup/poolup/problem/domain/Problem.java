package com.poolup.poolup.problem.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.interview.domain.InterviewHint;
import com.poolup.poolup.problem.enums.ProblemType;
import com.poolup.poolup.shared.domain.ExamProblem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Problem extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answer;

    @Enumerated(EnumType.STRING)
    private ProblemType problemType;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamProblem> examProblems;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultipleChoiceOption> answerChoices;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewHint> interviewHints;
}
