package com.pullup.member.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.member.domain.DeviceToken;
import com.pullup.member.domain.InterestSubject;
import com.pullup.member.domain.Member;
import com.pullup.member.domain.MemberExamStatistic;
import com.pullup.member.dto.DailySolvedHistoryDto;
import com.pullup.member.dto.request.DeviceTokenRequest;
import com.pullup.member.dto.request.InterestSubjectsRequest;
import com.pullup.member.dto.response.DailySolvedHistoryResponse;
import com.pullup.member.dto.response.MemberProfileResponse;
import com.pullup.member.repository.DeviceTokenRepository;
import com.pullup.member.repository.InterestSubjectRepository;
import com.pullup.member.repository.MemberExamStatisticRepository;
import com.pullup.member.repository.MemberRepository;
import com.pullup.problem.domain.Subject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final DeviceTokenRepository deviceTokenRepository;

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
        interestSubjectRepository.deleteAllByMemberId(memberId);

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

    @Transactional
    public void registerDeviceToken(Long memberId, DeviceTokenRequest deviceTokenRequest) {
        Member member = findMemberById(memberId);
        String token = deviceTokenRequest.deviceToken();

        if (!deviceTokenRepository.existsDeviceTokenByToken(token)) {
            DeviceToken deviceToken = DeviceToken.createDeviceToken(token, member);
            deviceTokenRepository.save(deviceToken);
        }
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public boolean isSolvedToday(Long memberId) {
        long solvedDays = findSolvedDaysById(memberId);
        return (solvedDays & 1) == 1;
    }

    public DailySolvedHistoryResponse getDailySolvedHistory(Long memberId) {
        long solvedDays = findSolvedDaysById(memberId);
        LocalDate today = LocalDate.now();
        List<DailySolvedHistoryDto> dailySolvedHistoryDtos = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            LocalDate date = today.minusDays(i);

            boolean isSolved = (solvedDays & (1L << i)) > 0;
            dailySolvedHistoryDtos.add(new DailySolvedHistoryDto(
                    isSolved ? 1 : 0,
                    date.toString(),
                    isSolved ? 1 : 0
            ));
        }

        dailySolvedHistoryDtos.sort(Comparator.comparing(DailySolvedHistoryDto::date));
        return DailySolvedHistoryResponse.of(dailySolvedHistoryDtos);
    }

    private long findSolvedDaysById(Long memberId) {
        return Optional.ofNullable(memberRepository.findSolvedDaysById(memberId)).orElse(0L);
    }

    public void updateSolveStatus(Member member) {
        Long solvedDays = member.getSolvedDays();
        solvedDays |= 1;

        member.updateSolvedDays(solvedDays);
        memberRepository.save(member);
    }

    @Transactional
    public void updateSolvedDaysWithLeftShift(Member member) {
        Long solvedDays = member.getSolvedDays();
        long newDaySolvedStatus = solvedDays << 1;

        member.updateSolvedDays(newDaySolvedStatus);
        memberRepository.save(member);
    }
}