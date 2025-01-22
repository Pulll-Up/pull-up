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
    ERR_EXAM_ROUND_INVALID("회차는 1 이상이어야 합니다.");

    private String message;


}
