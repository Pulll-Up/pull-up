package com.pullup.interview.dto.response;

import com.pullup.interview.dto.CommentsDto;
import java.util.List;


public record CommentsResponse(
        List<CommentsDto> comments
) {
    public static CommentsResponse of(List<CommentsDto> commentsDto) {
        return new CommentsResponse(commentsDto);
    }
}
