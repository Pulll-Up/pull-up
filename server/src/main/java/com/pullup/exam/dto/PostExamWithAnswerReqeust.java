package com.pullup.exam.dto;

import java.util.List;

public record PostExamWithAnswerReqeust(
        List<ProblemAndChosenAnswer> problemAndChosenAnswers
) {
}
