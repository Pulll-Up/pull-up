package com.pullup.exam.service;

import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.NotFoundException;
import com.pullup.exam.dto.ExamDetailsDto;
import com.pullup.exam.dto.ExamDetailsWithoutOptionsDto;
import com.pullup.exam.dto.GetExamDetailsResponse;
import com.pullup.exam.dto.PostExamDto;
import com.pullup.exam.repository.ExamRepository;
import com.pullup.problem.domain.ProblemOption;
import com.pullup.problem.repository.ProblemOptionRepository;
import com.pullup.problem.repository.ProblemRepository;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final ProblemOptionRepository problemOptionRepository;
    private final ProblemRepository problemRepository;

    public GetExamDetailsResponse getExamDetails(Long id) {
        List<ExamDetailsWithoutOptionsDto> examDetailsWithoutOptionsDtos = examRepository.findExamDetailsWithoutOptionsById(id);

        if (examDetailsWithoutOptionsDtos == null || examDetailsWithoutOptionsDtos.isEmpty()) {
            throw new NotFoundException(ErrorMessage.ERR_EXAM_NOT_FOUND);
        }

        List<ExamDetailsDto> examDetailsDtos = new ArrayList<>();
        for (ExamDetailsWithoutOptionsDto dto : examDetailsWithoutOptionsDtos) {
            List<ProblemOption> problemOptions = problemOptionRepository.findAllByProblemId(dto.problemId());
            List<String> contents = problemOptions.stream()
                    .map(ProblemOption::getContent)
                    .collect(Collectors.toList());

            examDetailsDtos.add(
                    new ExamDetailsDto(dto.problemId(), dto.problem(), contents, dto.subject().name()));
        }

        return new GetExamDetailsResponse(examDetailsDtos);

    }

    public Long postExam(PostExamDto postExamDto) {
        List<String> subjects = postExamDto.subjects();
        String difficultyLevel = postExamDto.difficultyLevel();

        // 선택한 과목의 문제들 중에서, 난이도 일치하는 문제들 랜덤으로 뽑아오기
        int baseCount = 20 / subjects.size();
        int remainCount = 20 % subjects.size();
        int index = 0;
        for (String subject : subjects) {
            int count = baseCount; // 기본 문제 수 설정
            if (index < remainCount) {
                count += 1; // 남은 문제를 분배
            }
            Pageable limit = (Pageable) PageRequest.of(0, count);

            problemRepository.findTopNBySubejctAndDifficultyLevelOrderByRandom(subject,difficultyLevel,limit);

            index++;
        }

    }

}
