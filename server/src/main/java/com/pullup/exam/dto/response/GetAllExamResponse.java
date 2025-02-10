package com.pullup.exam.dto.response;

import java.util.List;

public record GetAllExamResponse(
        List<GetExamResponse> getExamResponses
) {

    public static GetAllExamResponse of(List<GetExamResponse> GetExamResponse) {
        return new GetAllExamResponse(GetExamResponse);
    }
}
