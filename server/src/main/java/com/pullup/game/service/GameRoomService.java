package com.pullup.game.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final MemberRepository memberRepository;

    public CreateRoomResponse createRoom(Long memberId) {
        // 멤버 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND));

        // 게임방 생성
        GameRoom gameRoom = GameRoom.of(
                String.valueOf(memberId),
                member.getName()
        );

        // 게임방 저장
        gameRoomRepository.save(gameRoom);

        return CreateRoomResponse.of(
                gameRoom.getRoomId()
        );
    }


}
