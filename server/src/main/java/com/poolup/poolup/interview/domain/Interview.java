package com.poolup.poolup.interview.domain;

import com.poolup.poolup.common.domain.BaseEntity;
import com.poolup.poolup.shared.domain.MemberInterview;
import com.poolup.poolup.shared.domain.MemberInterviewAction;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Interview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String answer;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MemberInterview> memberInterviews;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MemberInterviewAction> memberInterviewActions;

    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    List<InterviewHint> interviewHints;

}
