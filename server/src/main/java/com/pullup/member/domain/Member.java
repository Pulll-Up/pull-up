package com.pullup.member.domain;

import com.pullup.auth.oAuth.domain.OAuth2UserInfo;
import com.pullup.auth.oAuth.domain.type.OAuthProvider;
import com.pullup.common.auditing.BaseTimeEntity;
import com.pullup.member.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String profileImageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_game_result_id")
    private MemberGameResult memberGameResult;

    @Builder
    private Member(String name, String email, Role role, OAuthProvider oAuthProvider, String providerId, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.oAuthProvider = oAuthProvider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
    }

    public static Member of(OAuth2UserInfo oAuth2UserInfo){
        return Member.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .role(Role.USER)
                .oAuthProvider(oAuth2UserInfo.getProvider())
                .providerId(oAuth2UserInfo.getProviderId())
                .profileImageUrl(oAuth2UserInfo.getProfileImageUrl())
                .build();
    }
}
