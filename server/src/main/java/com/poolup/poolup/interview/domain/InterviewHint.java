package com.poolup.poolup.interview.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.problem.domain.Problem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InterviewHint extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String keyword;

//    *** interview, problem 관계설정 해야함
    @ManyToOne @JoinColumn(name = "interview_id")
    private Interview interview;

    @ManyToOne @JoinColumn(name = "problem_id")
    private Problem problem;


}
