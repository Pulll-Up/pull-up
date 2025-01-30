package com.pullup.problem.dto;

import java.util.List;

public record GetAllWrongProblemsResponse(
        List<WrongProblemDto> wrongProblemDtos
) {
    public static GetAllWrongProblemsResponse of(List<WrongProblemDto> wrongProblemDtos) {
        return new GetAllWrongProblemsResponse(wrongProblemDtos);
    }
}
