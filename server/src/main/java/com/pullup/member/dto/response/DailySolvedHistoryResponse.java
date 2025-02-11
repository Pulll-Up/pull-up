package com.pullup.member.dto.response;

import com.pullup.member.dto.DailySolvedHistoryDto;
import java.util.List;

public record DailySolvedHistoryResponse(
        List<DailySolvedHistoryDto> dailySolvedHistories
) {
    public static DailySolvedHistoryResponse of(List<DailySolvedHistoryDto> dailySolvedHistories) {
        return new DailySolvedHistoryResponse(dailySolvedHistories);
    }
}