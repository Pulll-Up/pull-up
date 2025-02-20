package com.pullup.exam.dto.request;

import com.pullup.exam.dto.ProblemAndChosenAnswer;
import java.util.List;

public record PostExamWithAnswerRequest(
        List<ProblemAndChosenAnswer> problemAndChosenAnswers
) {
}
