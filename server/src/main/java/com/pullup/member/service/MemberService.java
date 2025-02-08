package com.pullup.member.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.member.domain.InterestSubject;
import com.pullup.member.domain.Member;
import com.pullup.member.domain.MemberExamStatistic;
import com.pullup.member.dto.request.InterestSubjectsRequest;
import com.pullup.member.dto.response.MemberProfileResponse;
import com.pullup.member.repository.InterestSubjectRepository;
import com.pullup.member.repository.MemberExamStatisticRepository;
import com.pullup.member.repository.MemberRepository;
import com.pullup.problem.domain.Subject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final InterestSubjectRepository interestSubjectRepository;
    private final MemberExamStatisticRepository memberExamStatisticRepository;

    @Transactional
    public void saveMemberExamStatistic(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));

        List<MemberExamStatistic> statistics = Arrays.stream(Subject.values())
                .map(subject -> MemberExamStatistic.of(subject, member))
                .collect(Collectors.toList());

        memberExamStatisticRepository.saveAll(statistics);
    }

    @Transactional
    public void saveInterestSubjects(Long memberId, List<String> subjectNames) {
        Member member = findMemberById(memberId);

        if (interestSubjectRepository.existsByMemberId(memberId)) {
            interestSubjectRepository.deleteAllByMemberId(memberId);
        }

        List<InterestSubject> interestSubjects = subjectNames.stream()
                .map(interestSubject -> InterestSubject.createInterestSubject(interestSubject, member))
                .toList();

        interestSubjectRepository.saveAll(interestSubjects);
    }

    public Boolean isExistInterestSubjects(Long memberId) {
        return interestSubjectRepository.existsByMemberId(memberId);
    }

    public MemberProfileResponse getMemberProfile(Long memberId) {
        Member member = findMemberById(memberId);
        List<String> interestSubjects = findInterestSubjectsByMemberId(memberId).stream()
                .map(InterestSubject::getSubject).toList();

        return MemberProfileResponse.of(member, interestSubjects);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));
    }

    @Transactional
    public void updateInterestSubject(Long memberId, InterestSubjectsRequest interestSubjectsRequest) {
        saveInterestSubjects(memberId, interestSubjectsRequest.subjectNames());
    }

    private List<InterestSubject> findInterestSubjectsByMemberId(Long memberId) {
        return interestSubjectRepository.findByMemberId(memberId);
    }
}