package com.pullup.exam.dto;

import java.util.List;

public record ExamResultDetailDto(
        Long problemId,
        String problem,
        List<String> options,
        String chosenAnswer,
        String answer,
        boolean answerStatus,
        boolean bookmarkStatus,
        String explanation,
        int correctRate,
        String round
) {
}