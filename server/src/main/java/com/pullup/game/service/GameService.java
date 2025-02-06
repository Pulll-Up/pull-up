package com.pullup.game.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.IllegalStatementException;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.domain.Player;
import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.ProblemCard;
import com.pullup.game.dto.request.CardSubmitRequest;
import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.GetPlayerNumberResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import com.pullup.problem.service.ProblemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRoomRepository gameRoomRepository;
    private final ProblemService problemService;
    private final MemberService memberService;
    
    public CreateRoomResponse createRoom(Long memberId, CreateRoomWithSubjectsRequest request) {
        Member member = memberService.findMemberById(memberId);

        GameRoom gameRoom = GameRoom.craeteGameRoomWithHost(
                memberId,
                member.getName()
        );

        gameRoomRepository.save(gameRoom);

        problemService.generateProblems(gameRoom.getRoomId(), request);

        return CreateRoomResponse.of(
                gameRoom.getRoomId()
        );
    }

    @Transactional
    public JoinRoomResponse join(String roomId, Long memberId) {
        GameRoom gameRoom = findByRoomId(roomId);
        // 방 상태가 waiting이 아니면, 방에 참가할 수 없다
        if (!gameRoom.getStatus().equals(GameRoomStatus.WAITING)) {
            throw new IllegalStatementException(ErrorMessage.ERR_GAME_ROOM_NOT_WAITING);
        }

        if (gameRoom.getPlayer1().getId() == memberId) {
            throw new IllegalStatementException(ErrorMessage.ERR_GAME_ROOM_MEMBER_DUPLICATED);
        }

        Member member = memberService.findMemberById(memberId);

        gameRoom.addGuest(member.getId(), member.getName());

        gameRoomRepository.save(gameRoom);

        return JoinRoomResponse.success();

    }

    public GameRoomStatus getGameRoomStatus(String roomId) {
        return findByRoomId(roomId).getStatus();
    }

    private GameRoom findByRoomId(String roomId) {
        return gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_GAME_ROOM_NOT_FOUND));
    }

    // 카드 선택 요청 처리
    public GameRoomInfoWithProblemsResponse processCardSubmission(CardSubmitRequest cardSubmitRequest) {

        GameRoom gameRoom = findByRoomId(cardSubmitRequest.roomId());

        List<ProblemCard> problemCards = getProblemsByRoomId(cardSubmitRequest.roomId());

        int problemNumber = cardSubmitRequest.problemNumber();
        for (ProblemCard problemCard : problemCards) {
            if (problemNumber == problemCard.getCardId()) {
                problemCard.disableCard();
            }
        }

        gameRoomRepository.saveProblems(gameRoom.getRoomId(), problemCards);

        // 플레이어 점수 업데이트
        Player player = gameRoom.getPlayerByPlayerId(cardSubmitRequest.playerId());
        player.increaseScore();

        gameRoomRepository.save(gameRoom);

        // 게임룸 정보 (상태) 업데이트
        if (isGameEnd(gameRoom.getPlayer1().getScore(), gameRoom.getPlayer2().getScore())) {
            gameRoom.updateStatusToFinished();
        }

        // 응답 객체 생성
        return GameRoomInfoWithProblemsResponse.of(
                gameRoom.getRoomId(),
                PlayerInfo.of(
                        gameRoom.getPlayer1().getId(),
                        gameRoom.getPlayer1().getName(),
                        gameRoom.getPlayer1().getScore()),
                PlayerInfo.of(
                        gameRoom.getPlayer2().getId(),
                        gameRoom.getPlayer2().getName(),
                        gameRoom.getPlayer2().getScore()),
                problemCards
        );
    }

    public List<ProblemCard> getProblemsByRoomId(String roomId) {
        return gameRoomRepository.getProblemsByRoomId(roomId);
    }

    private boolean isGameEnd(int player1Score, int player2Score) {
        if (player1Score + player2Score == 16) {
            return true;
        } else {
            return false;
        }
    }

    public GetPlayerNumberResponse getPlayerNumber(String roomId, Long memberId) {
        GameRoom gameRoom = findByRoomId(roomId);
        if (gameRoom.getPlayer1().getId() == memberId) {
            return GetPlayerNumberResponse.of(1L);
        } else if (gameRoom.getPlayer2().getId() == memberId) {
            return GetPlayerNumberResponse.of(2L);
        } else {
            throw new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND);
        }
    }

    public GameRoomInfoWithProblemsResponse getInitialGameRoomInfo(String roomId) {
        GameRoom gameRoom = findByRoomId(roomId);
        List<ProblemCard> problemCards = gameRoomRepository.getProblemsByRoomId(roomId);

        return GameRoomInfoWithProblemsResponse.of(
                roomId,
                PlayerInfo.from(gameRoom.getPlayer1()),
                PlayerInfo.from(gameRoom.getPlayer2()),
                problemCards
        );


    }


}
