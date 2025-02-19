package com.pullup.member.domain;

import com.pullup.common.auditing.BaseTimeEntity;
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
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Integer winCount;

    @Column(nullable = false)
    private Integer loseCount;

    @Column(nullable = false)
    private Integer drawCount;


    @Builder
    private MemberGameResult(Member member, Integer winCount, Integer loseCount, Integer drawCount) {
        this.member = member;
        this.winCount = winCount;
        this.loseCount = loseCount;
        this.drawCount = drawCount;
    }

    public void addWinCount() {
        this.winCount += 1;
    }

    public void addLoseCount() {
        this.loseCount += 1;
    }

    public void addDrawCount() {
        this.drawCount += 1;
    }

    public static MemberGameResult createMemberGameResult(Member member) {
        return MemberGameResult.builder()
                .member(member)
                .winCount(0)
                .loseCount(0)
                .drawCount(0)
                .build();
    }

    public Integer calculateWinningRate() {
        int totalGames = winCount + loseCount + drawCount;
        if (totalGames == 0) {
            return 0; // 게임을 한 적이 없으면 승률 0% 반환
        }
        return (int) Math.round(((double) winCount / totalGames) * 100);
    }


}
