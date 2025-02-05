package com.pullup.interview.dto.response;

import lombok.Builder;

@Builder
public record CommentResponse(
        Long commentId,
        String writer,
        String email,
        String content
) {
    public static CommentResponse of(Long commentId, String writer, String email, String content) {
        return CommentResponse.builder()
                .commentId(commentId)
                .writer(writer)
                .email(email)
                .content(content)
                .build();
    }
}