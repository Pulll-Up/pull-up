package com.pullup.exam.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GetExamPageResponse(
        List<GetExamResponse> content,
        int totalPages,
        int totalElements,
        boolean last
) {

    public static GetExamPageResponse of(List<GetExamResponse> content, int totalPages, int totalElements,
                                         boolean last) {
        return GetExamPageResponse.builder()
                .content(content)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .last(last)
                .build();
    }
}