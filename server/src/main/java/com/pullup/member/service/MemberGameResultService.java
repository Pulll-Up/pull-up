package com.pullup.member.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.dto.response.GameWinLoseDrawResultResponse;
import com.pullup.member.domain.MemberGameResult;
import com.pullup.member.repository.MemberGameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberGameResultService {

    private final MemberGameResultRepository memberGameResultRepository;

    public GameWinLoseDrawResultResponse getWinningRate(Long memberId) {
        MemberGameResult memberGameResult = findMemberGameResultByMemberId(memberId);

        return GameWinLoseDrawResultResponse.of(
                memberGameResult.getWinCount(),
                memberGameResult.getLoseCount(),
                memberGameResult.getDrawCount()
        );
    }

    private MemberGameResult findMemberGameResultByMemberId(Long memberId) {
        return memberGameResultRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_GAME_RESULT_NOT_FOUND));
    }
}
