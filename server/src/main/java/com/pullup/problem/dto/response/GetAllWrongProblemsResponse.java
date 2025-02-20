package com.pullup.problem.dto.response;

import com.pullup.problem.dto.WrongProblemDto;
import java.util.List;

public record GetAllWrongProblemsResponse(
        List<WrongProblemDto> wrongProblemDtos
) {
    public static GetAllWrongProblemsResponse of(List<WrongProblemDto> wrongProblemDtos) {
        return new GetAllWrongProblemsResponse(wrongProblemDtos);
    }
}
