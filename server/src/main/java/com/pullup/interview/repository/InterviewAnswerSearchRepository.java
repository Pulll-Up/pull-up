package com.pullup.interview.repository;

import com.pullup.interview.domain.InterviewAnswer;
import java.util.List;

public interface InterviewAnswerSearchRepository {
    List<InterviewAnswer> searchByKeyword(Long memberId, String keyword);
}
