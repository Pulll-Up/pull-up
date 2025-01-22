package com.pullup.exam.dto;

import java.util.List;

public record ExamWithAnswerReqeust(
        List<ProblemAndChosenAnswer> problemAndChosenAnswers
) {
}
