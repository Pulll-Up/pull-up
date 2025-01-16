package com.poolup.poolup.card.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberGameResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long winCount;
    private Long totalCount;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
