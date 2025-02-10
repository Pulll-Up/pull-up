package com.pullup.exam.dto.response;

import com.pullup.exam.dto.ExamStrengthDto;
import java.util.List;

public record GetExamStrengthResponse(
        List<ExamStrengthDto> examStrengthDtos
) {
}
