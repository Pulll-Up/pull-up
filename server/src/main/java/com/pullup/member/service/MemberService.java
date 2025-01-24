package com.pullup.member.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.member.domain.Member;
import com.pullup.member.domain.MemberExamStatistic;
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
@RequiredArgsConstructor
public class MemberService {

    private final MemberExamStatisticRepository memberExamStatisticRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveMemberExamStatic(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));

        List<MemberExamStatistic> statistics = Arrays.stream(Subject.values())
                .map(subject -> MemberExamStatistic.of(subject, member))
                .collect(Collectors.toList());

        memberExamStatisticRepository.saveAll(statistics);
    }


}
