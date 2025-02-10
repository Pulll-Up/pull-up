package com.pullup.exam.dto.request;

import com.pullup.common.vaildator.ValidDifficultyLevel;
import com.pullup.common.vaildator.ValidSubject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;


public record PostExamRequest(
        @NotEmpty(message = "과목 리스트는 비어있을 수 없습니다")
        @Size(min = 1, max = 6, message = "과목은 1개 이상 6개 이하여야 합니다")
        List<@ValidSubject String> subjects,

        @ValidDifficultyLevel
        @NotEmpty(message = "난이도는 필수 항목입니다")
        String difficultyLevel
) {
}