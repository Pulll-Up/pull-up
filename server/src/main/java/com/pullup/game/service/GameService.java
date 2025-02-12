package com.pullup.game.service;

import com.pullup.common.exception.BadRequestException;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.game.domain.GameRoom;
import com.pullup.game.domain.GameRoomStatus;
import com.pullup.game.domain.Player;
import com.pullup.game.dto.GameRoomResultStatus;
import com.pullup.game.dto.PlayerInfo;
import com.pullup.game.dto.ProblemCard;
import com.pullup.game.dto.ProblemCardWithoutCardId;
import com.pullup.game.dto.RandomMatchType;
import com.pullup.game.dto.request.CheckType;
import com.pullup.game.dto.request.CreateRoomWithSubjectsRequest;
import com.pullup.game.dto.request.SubmitCardRequest;
import com.pullup.game.dto.response.CreateRoomResponse;
import com.pullup.game.dto.response.GameRoomInfoWithProblemsResponse;
import com.pullup.game.dto.response.GameRoomResultResponse;
import com.pullup.game.dto.response.GetRandomMatchTypeResponse;
import com.pullup.game.dto.response.JoinRoomResponse;
import com.pullup.game.dto.response.PlayerResult;
import com.pullup.game.dto.response.PlayerType;
import com.pullup.game.repository.GameRoomRepository;
import com.pullup.member.domain.Member;
import com.pullup.member.service.MemberService;
import com.pullup.problem.service.ProblemService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private static final int TOTAL_SCORE = 8;

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

    public GameRoomInfoWithProblemsResponse checkTypeAndProcessCardSubmissionOrTimeout(
            SubmitCardRequest submitCardRequest) {
        // type 체크
        if (submitCardRequest.checkType().equals(CheckType.SUBMIT)) {
            GameRoomInfoWithProblemsResponse gameRoomInfoWithProblemsResponse = processCardSubmission(
                    submitCardRequest);

            return gameRoomInfoWithProblemsResponse;


        } else if (submitCardRequest.checkType().equals(CheckType.TIME_OVER)) {
            // 1. 방 상태 바꾸기
            // - finished로 바꾸기
            // - 이긴 사람 처리
            GameRoom gameRoom = findByRoomId(submitCardRequest.roomId());
            gameRoom.updateStatusToFinished();
            if (gameRoom.getPlayer1().getScore() > gameRoom.getPlayer2().getScore()) {
                gameRoom.updateWinner(gameRoom.getPlayer1());
            } else if (gameRoom.getPlayer1().getScore() < gameRoom.getPlayer2().getScore()) {
                gameRoom.updateWinner(gameRoom.getPlayer2());
            }

            // 2. GameRoomInfoWithProblemsResponse 만들기
            List<ProblemCard> problemCards = getProblemsByRoomId(submitCardRequest.roomId());

            return GameRoomInfoWithProblemsResponse.of(
                    gameRoom.getRoomId(),
                    GameRoomStatus.FINISHED,
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
        } else if (submitCardRequest.checkType().equals(CheckType.INIT)) {
            GameRoom gameRoom = findByRoomId(submitCardRequest.roomId());
            List<ProblemCard> problemCards = getProblemsByRoomId(submitCardRequest.roomId());

            return GameRoomInfoWithProblemsResponse.of(
                    gameRoom.getRoomId(),
                    GameRoomStatus.PLAYING,
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
        throw new BadRequestException(ErrorMessage.ERR_GAME_CHECK_TYPE_UNSUPPORTED);

    }

    private GameRoomInfoWithProblemsResponse processCardSubmission(SubmitCardRequest submitCardRequest) {

        GameRoom gameRoom = findByRoomId(submitCardRequest.roomId());

        List<ProblemCard> problemCards = getProblemsByRoomId(submitCardRequest.roomId());

        List<String> contents = submitCardRequest.contents();
        Long problemId1 = getProblemCardIdByContent(problemCards, contents.get(0));
        Long problemId2 = getProblemCardIdByContent(problemCards, contents.get(1));

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
        Player player = gameRoom.getPlayerByPlayerType(submitCardRequest.playerType());
        player.increaseScore();

        gameRoomRepository.save(gameRoom);

        // 끝났는지 확인
        // 게임룸 정보 (상태) 업데이트
        GameRoomStatus nowGameRoomState = gameRoom.getGameRoomStatus();
        if (isGameEnd(gameRoom.getPlayer1().getScore(), gameRoom.getPlayer2().getScore(), gameRoom.getRoomId())) {
            // 1. 상태 finish 바꾸기
            // 2. winner update 하기
            gameRoom.updateStatusToFinished();
            nowGameRoomState = GameRoomStatus.FINISHED;
            updateGameRoomWinner(gameRoom, gameRoom.getPlayer1(), gameRoom.getPlayer2());
        }

        return GameRoomInfoWithProblemsResponse.of(
                gameRoom.getRoomId(),
                nowGameRoomState,
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

    private void updateGameRoomWinner(GameRoom gameRoom, Player player1, Player player2) {
        if (player1.getScore() > player2.getScore()) {
            gameRoom.updateWinner(player1);
        } else if (player1.getScore() < player2.getScore()) {
            gameRoom.updateWinner(player2);
        }
    }

    public List<ProblemCard> getProblemsByRoomId(String roomId) {
        return gameRoomRepository.getProblemsByRoomId(roomId);
    }

    private boolean isGameEnd(int player1Score, int player2Score, String roomId) {
        log.info("문제 개수: {}", getProblemsByRoomId(roomId).size());
        if (player1Score + player2Score == (getProblemsByRoomId(roomId).size()) / 2) {
            return true;
        } else {
            return false;
        }
    }

    public PlayerType getPlayerNumberByMemberId(String roomId, Long memberId) {
        GameRoom gameRoom = findByRoomId(roomId);
        if (gameRoom.getPlayer1().getId() == memberId) {
            return PlayerType.player1P;
        } else if (gameRoom.getPlayer2().getId() == memberId) {
            return PlayerType.player2P;
        } else {
            throw new NotFoundException(ErrorMessage.ERR_MEMBER_NOT_FOUND);
        }
    }

    public GameRoomInfoWithProblemsResponse getInitialGameRoomInfo(String roomId) {
        GameRoom gameRoom = findByRoomId(roomId);
        List<ProblemCard> problemCards = gameRoomRepository.getProblemsByRoomId(roomId);

        return GameRoomInfoWithProblemsResponse.of(
                roomId,
                gameRoom.getGameRoomStatus(),
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

    // 안섞은 버전
    public List<ProblemCardWithoutCardId> convertToProblemCardWithoutCardIds(List<ProblemCard> problemCards) {
        return problemCards.stream()
                .map(ProblemCardWithoutCardId::from)
                .collect(Collectors.toList());
    }

//    // 섞은 버전
//    private List<ProblemCardWithoutCardId> convertToProblemCardWithoutCardIds(List<ProblemCard> problemCards) {
//        return problemCards.stream()
//                .map(ProblemCardWithoutCardId::from)
//                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
//                    Collections.shuffle(list); // 변환 후 리스트를 무작위로 섞음
//                    return list;
//                }));
//    }

    public void deleteGameRoom(String roomId) {
        gameRoomRepository.deleteGameRoomAndProblems(roomId);
    }

    public GameRoomResultResponse getGameRoomResult(String roomId) {
        GameRoom gameRoom = findByRoomId(roomId);

        Player winner = gameRoom.getWinner();

        // 1. 방 이탈한 경우
        if (gameRoom.getIsForfeitGame()) {
            if (winner.getId() == gameRoom.getPlayer1().getId()) { // 1P가 승자인 경우
                return GameRoomResultResponse.of(
                        false,
                        true,
                        PlayerResult.of(
                                gameRoom.getPlayer1().getName(),
                                gameRoom.getPlayer1().getScore(),
                                GameRoomResultStatus.WIN
                        ),
                        PlayerResult.of(
                                gameRoom.getPlayer2().getName(),
                                gameRoom.getPlayer2().getScore(),
                                GameRoomResultStatus.LOSE
                        )
                );
            } else {
                return GameRoomResultResponse.of( // 2P가 승자인 경우
                        false,
                        true,
                        PlayerResult.of(
                                gameRoom.getPlayer1().getName(),
                                gameRoom.getPlayer1().getScore(),
                                GameRoomResultStatus.LOSE
                        ),
                        PlayerResult.of(
                                gameRoom.getPlayer2().getName(),
                                gameRoom.getPlayer2().getScore(),
                                GameRoomResultStatus.WIN
                        )
                );
            }
        }

        // 2. 방 이탈 아닌 경우 - 정상 종료 or 타임 아웃
        // 2-1. 1P 승리
        else if (winner.getId() == gameRoom.getPlayer1().getId()) {
            return GameRoomResultResponse.of(
                    false,
                    false,
                    PlayerResult.of(
                            gameRoom.getPlayer1().getName(),
                            gameRoom.getPlayer1().getScore(),
                            GameRoomResultStatus.WIN
                    ),
                    PlayerResult.of(
                            gameRoom.getPlayer2().getName(),
                            gameRoom.getPlayer2().getScore(),
                            GameRoomResultStatus.LOSE
                    )
            );
        }
        // 2P가 승자인 경우
        else if (winner.getId() == gameRoom.getPlayer2().getId()) {
            return GameRoomResultResponse.of(
                    false,
                    false,
                    PlayerResult.of(
                            gameRoom.getPlayer1().getName(),
                            gameRoom.getPlayer1().getScore(),
                            GameRoomResultStatus.LOSE
                    ),
                    PlayerResult.of(
                            gameRoom.getPlayer2().getName(),
                            gameRoom.getPlayer2().getScore(),
                            GameRoomResultStatus.WIN
                    )
            );
        } else {
            return GameRoomResultResponse.of(
                    true,
                    false,
                    PlayerResult.of(
                            gameRoom.getPlayer1().getName(),
                            gameRoom.getPlayer1().getScore(),
                            GameRoomResultStatus.DRAW
                    ),
                    PlayerResult.of(
                            gameRoom.getPlayer2().getName(),
                            gameRoom.getPlayer2().getScore(),
                            GameRoomResultStatus.DRAW
                    )
            );
        }

    }


}

