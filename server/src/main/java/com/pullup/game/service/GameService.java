package com.pullup.game.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRoomRepository gameRoomRepository;
    private final MemberService memberService;

    public CreateRoomResponse createRoom(Long memberId) {
        // 멤버 정보 조회
        Member member = memberService.findMemberById(memberId);

        // 게임방 생성
        GameRoom gameRoom = GameRoom.craeteGameRoomWithHost(
                memberId,
                member.getName()
        );

        // 게임방 저장
        gameRoomRepository.save(gameRoom);

        return CreateRoomResponse.of(
                gameRoom.getRoomId()
        );
    }

    @Transactional
    public JoinRoomResponse join(String roomId, Long memberId) {
        GameRoom gameRoom = findByRoomId(roomId);
        Member member = memberService.findMemberById(memberId);

        gameRoom.addGuest(member.getId(), member.getName());

        gameRoomRepository.save(gameRoom); // 변경된 상태 저장

        return JoinRoomResponse.of(true);

    }

    private GameRoom findByRoomId(String roomId) {
        return gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_GAME_ROOM_NOT_FOUND));
    }


}
