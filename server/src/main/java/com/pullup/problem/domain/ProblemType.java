package com.pullup.problem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProblemType {
    MULTIPLE_CHOICE("객관식"),
    SHORT_ANSWER("주관식");

    private final String description;
}