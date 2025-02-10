package com.pullup.exam.dto.response;

import com.pullup.exam.dto.ExamDetailsDto;
import java.util.List;

public record GetExamDetailsResponse(
        List<ExamDetailsDto> examDetailsDtos
) {
}
