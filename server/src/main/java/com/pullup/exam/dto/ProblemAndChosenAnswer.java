package com.pullup.exam.dto;

import com.pullup.common.annotation.DecryptedId;

public record ProblemAndChosenAnswer(
        @DecryptedId Long problemId,
        String chosenAnswer
) {
}
