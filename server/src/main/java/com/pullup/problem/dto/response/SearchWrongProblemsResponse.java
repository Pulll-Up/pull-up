package com.pullup.problem.dto.response;

import com.pullup.problem.dto.WrongProblemDto;
import java.util.List;

public record SearchWrongProblemsResponse(
        List<WrongProblemDto> wrongProblemDtos
) {
    public static SearchWrongProblemsResponse of(List<WrongProblemDto> wrongProblemDtos) {
        return new SearchWrongProblemsResponse(wrongProblemDtos);
    }
}
