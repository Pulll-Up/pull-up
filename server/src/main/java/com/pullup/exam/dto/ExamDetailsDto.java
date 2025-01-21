package com.pullup.exam.dto;

import java.util.List;

public record ExamDetailsDto(
        Long problemId,
        String problem,
        List<String> options,
        String subject
) {
}
