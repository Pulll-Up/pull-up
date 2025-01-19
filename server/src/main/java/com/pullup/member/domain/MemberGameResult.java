package com.pullup.member.domain;

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
@Table(name = "member_game_result")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGameResult extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer winCount;

    @Column(nullable = false)
    private Integer totalCount;
}
