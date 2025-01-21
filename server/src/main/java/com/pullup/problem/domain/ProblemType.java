package com.pullup.problem.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProblemType {
    MULTIPLE_CHOICE("객관식"),
    SHORT_ANSWER("주관식");

    private final String description;
}