package com.poolup.poolup.exam.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.member.domain.Member;
import com.poolup.poolup.shared.domain.ExamProblem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Exam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long Score;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamProblem> examProblems;
}
