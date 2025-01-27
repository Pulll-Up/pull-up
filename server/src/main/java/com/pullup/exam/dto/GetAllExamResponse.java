package com.pullup.exam.dto;

import java.util.List;

public record GetAllExamResponse(
        List<GetExamResponse> getExamResponses
) {
    public static GetAllExamResponse of(List<GetExamResponse> getExamResponses) {
        return new GetAllExamResponse(getExamResponses);
    }
}
