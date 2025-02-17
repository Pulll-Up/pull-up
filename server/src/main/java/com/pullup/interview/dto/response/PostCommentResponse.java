package com.pullup.interview.dto.response;

public record PostCommentResponse(
        String commentId
) {
    public static PostCommentResponse of(String commentId) {
        return new PostCommentResponse(commentId);
    }
}
