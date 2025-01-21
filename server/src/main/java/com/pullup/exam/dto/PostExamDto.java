package com.pullup.exam.dto;

import java.util.List;

public record PostExamDto(
        List<String> subjects,
        String difficultyLevel
) {
}
