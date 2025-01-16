package com.poolup.poolup.subject.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.member.domain.Member;
import com.poolup.poolup.subject.enums.SubjectName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InterestSubject extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private SubjectName subjectName;

    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;
}
