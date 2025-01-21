package com.pullup.exam.dto;

import java.util.List;

public record GetExamDetailsResponse(
        Long problemId,
        String problem,
        List<String> options,
        String subject
) {
}
