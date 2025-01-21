package com.pullup.exam.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.repository.ExamRepository;
import com.pullup.problem.domain.ProblemOption;
import com.pullup.problem.repository.ProblemOptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ProblemOptionRepository problemOptionRepository;

    public List<GetExamDetailsResponse> getExamDetails(Long id) {
        List<ExamDetailsWithoutOptionsDto> examDetailsWithoutOptionsDtos = examRepository.findExamDetailsWithoutOptionsById(id);

        if (examDetailsWithoutOptionsDtos == null) {
            throw new NotFoundException(ErrorMessage.ERR_EXAM_NOT_FOUND);
        }

        List<GetExamDetailsResponse> getExamDetailsResponses = new ArrayList<>();
        for (ExamDetailsWithoutOptionsDto dto : examDetailsWithoutOptionsDtos) {
            List<ProblemOption> problemOptions = problemOptionRepository.findAllByProblemId(dto.problemId());
            List<String> contents = problemOptions.stream()
                    .map(ProblemOption::getContent)
                    .collect(Collectors.toList());

            getExamDetailsResponses.add(
                    new GetExamDetailsResponse(dto.problemId(), dto.problem(), contents, dto.subject().name()));
        }

        return getExamDetailsResponses;

    }

}
