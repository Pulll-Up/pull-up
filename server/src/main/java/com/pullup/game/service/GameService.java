package com.pullup.game.service;

import com.pullup.common.exception.BadRequestException;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.domain.Player;
import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.ProblemCard;
import com.pullup.game.dto.ProblemCardWithoutCardId;
import com.pullup.game.dto.RandomMatchType;
import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.dto.request.SubmitCardRequest;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.GetPlayerNumberResponse;
import com.pullup.game.dto.response.GetRandomMatchTypeResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import com.pullup.problem.service.ProblemService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRoomRepository gameRoomRepository;
    private final ProblemService problemService;
    private final MemberService memberService;

    public CreateRoomResponse createRoom(Long memberId, CreateRoomWithSubjectsRequest request) {
        Member member = memberService.findMemberById(memberId);

        GameRoom gameRoom = GameRoom.createGameRoomByInvitationWithHost(
                memberId,
                member.getName()
        );

        gameRoomRepository.save(gameRoom);

        problemService.generateProblems(gameRoom.getRoomId(), request);

        return CreateRoomResponse.of(
                gameRoom.getRoomId()
        );
    }

    public CreateRoomResponse createRoomForRandomMatching(Long memberId) {
        Member member = memberService.findMemberById(memberId);

        GameRoom gameRoom = GameRoom.createGameRoomByRandomMatchingWithHost(
                memberId,
                member.getName()
        );

        gameRoomRepository.save(gameRoom);

        problemService.generateProblemsForRandomMatching(gameRoom.getRoomId());

        return CreateRoomResponse.of(
                gameRoom.getRoomId()
        );
    }

    public JoinRoomResponse join(String roomId, Long memberId) {
        GameRoom gameRoom = findByRoomId(roomId);

        if (!gameRoom.getGameRoomStatus().equals(GameRoomStatus.WAITING)) {
            throw new BadRequestException(ErrorMessage.ERR_GAME_ROOM_NOT_WAITING);
        }

        if (gameRoom.getPlayer1().getId() == memberId) {
            throw new BadRequestException(ErrorMessage.ERR_GAME_ROOM_MEMBER_DUPLICATED);
        }

        Member member = memberService.findMemberById(memberId);

        gameRoom.addGuest(member.getId(), member.getName());

        gameRoomRepository.save(gameRoom);

        return JoinRoomResponse.success();

    }


    public GameRoomStatus getGameRoomStatus(String roomId) {
        return findByRoomId(roomId).getGameRoomStatus();
    }

    private GameRoom findByRoomId(String roomId) {
        return gameRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ERR_GAME_ROOM_NOT_FOUND));
    }

    // 카드 선택 요청 처리
    public GameRoomInfoWithProblemsResponse processCardSubmission(SubmitCardRequest submitCardRequest) {

        GameRoom gameRoom = findByRoomId(submitCardRequest.roomId());

        List<ProblemCard> problemCards = getProblemsByRoomId(submitCardRequest.roomId());

        List<String> contents = submitCardRequest.contents();
        Long problemId1 = getProblemCardIdByContent(problemCards, contents.get(0));
        Long problemId2 = getProblemCardIdByContent(problemCards, contents.get(0));

        // 틀림
        if (problemId1 != problemId2) {
            throw new BadRequestException(ErrorMessage.ERR_GAME_CARD_SUBMIT_WRONG);
        }

        // 정답
        for (ProblemCard problemCard : problemCards) {
            if (problemCard.getCardId() == problemId1) {
                problemCard.disableCard(); // 정답 처리
            }
        }

        gameRoomRepository.saveProblems(gameRoom.getRoomId(), problemCards);

        // 플레이어 점수 업데이트
        Player player = gameRoom.getPlayerByPlayerId(submitCardRequest.playerId());
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
                convertToProblemCardWithoutCardIds(problemCards)
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
                convertToProblemCardWithoutCardIds(problemCards)
        );


    }

    public GetRandomMatchTypeResponse getRandomMatchType() {
        List<GameRoom> gameRooms = new ArrayList<>(gameRoomRepository.findAll());

        if (gameRooms.isEmpty()) {
            return GetRandomMatchTypeResponse.createForCreateType(RandomMatchType.CREATE);
        } else {
            String roomId = gameRooms.get(0).getRoomId();
            return GetRandomMatchTypeResponse.createForJoinType(RandomMatchType.JOIN, roomId);
        }
    }

    private Long getProblemCardIdByContent(List<ProblemCard> problemCards, String content) {
        for (ProblemCard problemCard : problemCards) {
            if (problemCard.getContent().equals(content)) {
                return problemCard.getCardId();
            }
        }
        throw new NotFoundException(ErrorMessage.ERR_CONTENT_NOT_FOUND);
    }

    private List<ProblemCardWithoutCardId> convertToProblemCardWithoutCardIds(List<ProblemCard> problemCards) {
        return problemCards.stream()
                .map(ProblemCardWithoutCardId::from)
                .collect(Collectors.toList());
    }

    public void deleteGameRoom(String roomId) {
        gameRoomRepository.deleteGameRoomAndProblems(roomId);
    }

}
