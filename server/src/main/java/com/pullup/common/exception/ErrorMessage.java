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

    //* Interview 관련 Exception
    ERR_INTERVIEW_NOT_FOUND("면접 질문을 찾을 수 없습니다."),

    //* OAuth2 관련 Exception
    ERR_UNSUPPORTED_OAUTH2_PROVIDER("지원하지 않는 OAUTH2 제공자입니다."),
    ERR_FAILURE_OAUTH2_AUTHENTICATION("OAuth2 로그인에 실패하였습니다."),

    //* Cookie 관련 Exception,
    ERR_COOKIE_NOT_FOUND("쿠키를 찾을 수 없습니다");

    private final String message;
}
