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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "device_token")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class DeviceToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public DeviceToken(String token, Member member) {
        this.token = token;
        this.member = member;
    }

    public static DeviceToken createDeviceToken(String token, Member member) {
        return DeviceToken.builder()
                .token(token)
                .member(member)
                .build();
    }
}
