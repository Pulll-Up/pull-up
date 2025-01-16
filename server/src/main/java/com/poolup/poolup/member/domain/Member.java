package com.poolup.poolup.member.domain;

import com.poolup.poolup.card.domain.MemberGameResult;
import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.exam.domain.Exam;
import com.poolup.poolup.shared.domain.MemberInterview;
import com.poolup.poolup.shared.domain.MemberInterviewAction;
import com.poolup.poolup.subject.domain.InterestSubject;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// *** CascadeType.All 이 최선인지 다시 찾아봐야 함(석환)
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String role;
    private String authProvider;
    private String providerId;
    private String profileImageUrl;

    @OneToOne(mappedBy = "member")
    private MemberGameResult memberGameResult;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterestSubject> interestSubjects;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterview> MemberInterviews;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterviewAction> memberInterviewActions;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberExamStatistic> memberExamStatistics;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> exams;


}
