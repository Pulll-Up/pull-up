package com.pullup.member.service;

import com.pullup.member.repository.MemberHistoryRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberHistoryService {
    private final MemberHistoryRepository memberHistoryRepository;

    public boolean isExistMemberHistory(Long memberId) {
        return memberHistoryRepository.existsByMemberId(memberId);
    }

    public boolean isSolvedToday(Long memberId) {
        LocalDate today = LocalDate.now();
        return memberHistoryRepository.existsByMemberIdAndIsTodaySolved(memberId, today);
    }
}
