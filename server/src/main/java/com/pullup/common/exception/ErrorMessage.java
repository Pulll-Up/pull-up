package com.pullup.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    ERR_EXAM_NOT_FOUND("모의고사를 찾을 수 없습니다");

    private String message;


}
