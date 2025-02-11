package com.pullup.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record DailySolvedHistoryDto(
        @Schema(description = "문제를 푼 경우는 1, 아니면 0", example = "1")
        Integer count,
        @Schema(description = "날짜", example = "2025-02-11")
        String date,
        @Schema(description = "문제를 푼 경우는 1, 아니면 0", example = "1")
        Integer level
) {
    public static DailySolvedHistoryDto of(
            Integer count,
            String date,
            Integer level
    ) {
        return builder()
                .count(count)
                .date(date)
                .level(level)
                .build();
    }
}
