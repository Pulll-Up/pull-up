package com.pullup.exam.dto;

import com.pullup.exam.domain.Exam;
import com.pullup.problem.domain.Subject;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

public record GetExamResponse(
        Long examId,
        String examName,
        LocalDateTime date,
        List<Subject> subjects
) {
    @Builder
    public GetExamResponse {
    }

    public static GetExamResponse of(Exam exam, List<Subject> subjects) {
        return GetExamResponse.builder()
                .examId(exam.getId())
                .examName(String.format("제 %d회 모의고사", exam.getRound()))
                .date(exam.getCreatedAt())
                .subjects(subjects)
                .build();
    }
}
