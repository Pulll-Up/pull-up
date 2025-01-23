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
    ERR_EXAM_PROBLEM_NOT_FOUND("해당 시험에 대한 해당 문제가 존재하지 않습니다"),

    //* OAuth2 관련 에러
    ERR_UNSUPPORTED_OAUTH2_PROVIDER("지원하지 않는 OAUTH2 제공자입니다"),
    ;

    private String message;
}
