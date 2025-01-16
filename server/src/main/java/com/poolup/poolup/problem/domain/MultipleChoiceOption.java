package com.poolup.poolup.problem.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MultipleChoiceOption extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String optionNumber;
    private String content;

    @ManyToOne @JoinColumn(name = "problem_id")
    private Problem problem;
}
