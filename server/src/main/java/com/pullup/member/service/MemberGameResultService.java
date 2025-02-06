package com.pullup.member.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.IllegalStatementException;
import com.pullup.common.exception.NotFoundException;
import com.pullup.member.domain.MemberGameResult;
import com.pullup.member.repository.MemberGameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberGameResultService {

    private final MemberGameResultRepository memberGameResultRepository;

    public MemberGameResult findMemberGameResultByMemberId(Long memberId) {
        List<MemberGameResult> memberGameResults = memberGameResultRepository.findByMemberId(memberId);

        if (memberGameResults.isEmpty()) {
            throw new NotFoundException(ErrorMessage.ERR_MEMBER_GAME_RESULT_NOT_FOUND);
        }

        if (memberGameResults.size() > 1) {
            throw new IllegalStatementException(ErrorMessage.ERR_MEMBER_GAME_RESULT_COUNT_EXCEED);
        }

        return memberGameResults.get(0);
    }
}
