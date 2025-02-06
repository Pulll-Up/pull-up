package com.pullup.interview.dto.response;

public record PostCommentResponse(
        Long commentId
) {
    public static PostCommentResponse of(Long commentId) {
        return new PostCommentResponse(commentId);
    }
}
