package com.pullup.problem.domain;

import com.pullup.common.auditing.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(nullable = false)
    private Integer correctRate;
}
