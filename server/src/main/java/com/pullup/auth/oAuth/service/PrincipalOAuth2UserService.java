package com.pullup.auth.oAuth.service;

import com.pullup.auth.oAuth.domain.OAuth2UserInfo;
import com.pullup.auth.oAuth.domain.OAuth2UserInfoFactory;
import com.pullup.auth.oAuth.domain.PrincipalDetail;
import com.pullup.member.domain.Member;
import com.pullup.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2User, userRequest);

        String email = oAuth2UserInfo.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.of(oAuth2UserInfo)));

        return new PrincipalDetail(member, oAuth2User.getAttributes());
    }
}