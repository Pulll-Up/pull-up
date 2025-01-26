package com.pullup.problem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pullup.problem.domain.Subject;

public record RecentWrongQuestionDto(
        Long problemId,
        String question,
        @JsonProperty("subject")
        Subject subject
) {

}
