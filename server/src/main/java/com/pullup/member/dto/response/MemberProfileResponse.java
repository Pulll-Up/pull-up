package com.pullup.member.dto.response;

import com.pullup.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record MemberProfileResponse(
        @Schema(description = "회원 이름", example = "이석환")
        String name,
        @Schema(description = "회원 이메일", example = "im2shandyou@gmail.com")
        String email,
        @Schema(description = "회원 프로필 이미지 URL", example = "https://lh3.googleusercontent.com/a/ACg8ocLdOrn_oTb46ocN1PsRP_-bj1ISDi-05HDKuopwChRmIgVCnQ=s96-c")
        String profileImageUrl,
        @Schema(description = "회원 관심 과목", example = "[OS, NETWORK]")
        List<String> interestSubjects
) {

    public static MemberProfileResponse of(Member member, List<String> interestSubjects) {
        return MemberProfileResponse.builder()
                .name(member.getName())
                .email(member.getEmail())
                .profileImageUrl(member.getProfileImageUrl())
                .interestSubjects(interestSubjects)
                .build();
    }
}
