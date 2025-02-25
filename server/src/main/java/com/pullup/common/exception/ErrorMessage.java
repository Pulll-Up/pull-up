package com.pullup.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {

    ERR_EXAM_NOT_FOUND("모의고사를 찾을 수 없습니다"),
    ERR_MEMBER_NOT_FOUND("멤버를 찾을 수 없습니다"),
    ERR_INSUFFICIENT_PROBLEMS("문제 수가 충분하지 않습니다"),
    ERR_EXAM_SCORE_INVALID("시험 점수는 0에서 100 사이여야 합니다."),
    ERR_EXAM_ROUND_INVALID("회차는 1 이상이어야 합니다."),
    ERR_PROBLEM_NOT_FOUND("문제를 찾을 수 없습니다"),
    ERR_MEMBER_EXAM_STATISTIC_NOT_FOUND("회원 문제 통계를 찾을 수 없습니다"),
    ERR_EXAM_PROBLEM_NOT_FOUND("해당 시험에 대한 해당 문제가 존재하지 않습니다"),

    //* Bookmark 관련 Exception
    ERR_BOOKMARK_NOT_FOUND("북마크 결과를 찾을 수 없습니다"),

    //* Interview 관련 Exception
    ERR_INTERVIEW_NOT_FOUND("면접 질문을 찾을 수 없습니다."),
    ERR_INTERVIEW_ANSWER_NOT_FOUND("면접 답변을 찾을 수 없습니다."),
    ERR_INTERVIEW_NOT_ENOUGH("면접 질문이 충분하지 않습니다."),
    ERR_INTERVIEW_ALREADY_ANSWERED("이미 답변한 면접 질문입니다."),

    //* OAuth2 관련 Exception
    ERR_UNSUPPORTED_OAUTH2_PROVIDER("지원하지 않는 OAUTH2 제공자입니다."),
    ERR_FAILURE_OAUTH2_AUTHENTICATION("OAuth2 로그인에 실패하였습니다."),

    //* Cookie 관련 Exception,
    ERR_COOKIE_NOT_FOUND("쿠키를 찾을 수 없습니다"),

    //* Game 관련 Exception
    ERR_GAME_ROOM_NOT_FOUND("게임 방을 찾을 수 없습니다"),
    ERR_GAME_ROOM_NOT_WAITING("게임이 이미 진행 중인 방이거나, 끝난 방입니다"),
    ERR_GAME_ROOM_MEMBER_DUPLICATED("게임 방에 이미 존재하는 플레이어입니다"),
    ERR_CONTENT_NOT_FOUND("문제 리스트에서 일치하는 질문이나 정답을 찾을 수 없습니다"),
    ERR_GAME_CARD_SUBMIT_WRONG("제출한 질문과 정답이 일치하지 않습니다"),
    ERR_GAME_CHECK_TYPE_UNSUPPORTED("게임 체크 타입이 올바르지 않습니다. SUBMIT 또는 TIME_OUT만 허용됩니다"),
    ERR_GAME_CARD_ALREADY_SUBMITTED("이미 다른 사용자가 선택한 카드입니다"),
    ERR_GAME_CARD_NOT_FOUND("카드를 찾을 수 없습니다"),
    ERR_GAME_ROOM_ALREADY_FULL("이미 방이 꽉 찼습니다"),
    ERR_MEMBER_GAME_RESULT_NOT_FOUND("게임 결과 테이블을 찾을 수 없습니다"),

    //* Comment 관련 Exception
    ERR_COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다"),
    ERR_GAME_PROBLEM_LACK("해당 과목에 대한 게임 문제 수가 충분하지 않습니다"),

    ERR_PLAYER_NOT_FOUND("플레이어를 찾을 수 없습니다"),

    //* FCM 관련 Exception
    ERR_FCM_FAILED_TO_SEND("FCM 메시지 전송에 실패하였습니다"),

    // TODO : 명확한 네이밍으로 수정 필요
    //* Server 내부 Exception
    ERR_INTERNAL_SERVER("알 수 없는 서버의 오류입니다"),
    ERR_PARSING_GPT_RESPONSE_FAIL("GPT 응답을 파싱하던 중 오류가 발생했습니다"),
    ERR_INTERNAL_SERVER_ENCRYPTION_ERROR("암호화 중 오류가 발생했습니다"),
    ERR_INTERNAL_SERVER_DECRYPTION_ERROR("복호화 중 오류가 발생했습니다");

    private final String message;
}
