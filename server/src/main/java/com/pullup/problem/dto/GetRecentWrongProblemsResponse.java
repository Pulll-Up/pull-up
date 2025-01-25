package com.pullup.problem.dto;

import java.util.List;

public record GetRecentWrongProblemsResponse(
        List<RecentWrongQuestionDto> recentWrongQuestionDtos
) {
}
