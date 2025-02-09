package com.pullup.interview.repository;

import static com.pullup.interview.domain.QInterview.interview;
import static com.pullup.interview.domain.QInterviewAnswer.interviewAnswer;

import com.pullup.interview.domain.InterviewAnswer;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class InterviewAnswerSearchRepositoryImpl implements InterviewAnswerSearchRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<InterviewAnswer> searchByKeyword(Long memberId, String keyword) {
        return queryFactory
                .selectFrom(interviewAnswer)
                .innerJoin(interviewAnswer.interview, interview).fetchJoin()
                .where(
                        Objects.requireNonNull(containsKeyword(keyword))
                                .and(interviewAnswer.member.id.eq(memberId))
                )
                .fetch();
    }


    private BooleanExpression containsKeyword(String keyword) {
        String processedKeyword = keyword.trim();
        if (StringUtils.isEmpty(processedKeyword)) {
            return null;
        }
        return interview.question.contains(processedKeyword);
    }
}