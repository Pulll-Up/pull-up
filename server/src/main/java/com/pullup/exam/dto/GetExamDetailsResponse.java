package com.pullup.exam.dto;

import java.util.List;

public record GetExamDetailsResponse(
        List<ExamDetailsDto> examDetailsDtos
) {
}
