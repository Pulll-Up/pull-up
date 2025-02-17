package com.pullup.exam.dto.response;

import com.pullup.exam.domain.Exam;
import com.pullup.problem.domain.Subject;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GetExamResponse(
        String examId,
        String examName,
        LocalDateTime date,
        List<Subject> subjects
) {
    public static GetExamResponse of(String examId, Exam exam, List<Subject> subjects) {
        return GetExamResponse.builder()
                .examId(examId)
                .examName(String.format("제 %d회 모의고사", exam.getRound()))
                .date(exam.getCreatedAt())
                .subjects(subjects)
                .build();
    }
}
