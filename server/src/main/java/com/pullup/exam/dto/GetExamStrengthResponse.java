package com.pullup.exam.dto;

import java.util.List;

public record GetExamStrengthResponse(
        List<ExamStrengthDto> examStrengthDtos
) {
}
