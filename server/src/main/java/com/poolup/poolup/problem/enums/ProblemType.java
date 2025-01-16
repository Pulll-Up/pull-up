package com.poolup.poolup.problem.enums;

import lombok.Getter;

@Getter
public enum ProblemType {
    MULTIPLE("객관식")
    , SUBJECTIVE("주관식");

    private final String displayName;

    ProblemType(String displayName) {
        this.displayName = displayName;
    }
}
