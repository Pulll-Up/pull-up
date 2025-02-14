package com.pullup.problem.dto.response;

import com.pullup.problem.dto.RecentWrongQuestionDto;
import java.util.List;

public record GetRecentWrongProblemsResponse(
        List<RecentWrongQuestionDto> recentWrongQuestionDtos
) {
}
