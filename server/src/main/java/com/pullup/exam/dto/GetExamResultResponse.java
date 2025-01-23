package com.pullup.exam.dto;

import java.util.List;

public record GetExamResultResponse(
        List<ExamResultDetailDto> examResults
) {

}