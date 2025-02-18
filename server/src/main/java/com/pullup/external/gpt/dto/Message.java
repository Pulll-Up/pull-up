package com.pullup.external.gpt.dto;

public record Message(
        String role,
        String content
) {
    public static Message of(String role, String content) {
        return new Message(role, content);
    }
}


