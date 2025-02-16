package com.pullup.interview.dto.response;

public record ChatGptResponse(
        String strength,
        String weakness
) {

    public String getGptMessageContent() {
        return String.format("{\"strength\": \"%s\", \"weakness\": \"%s\"}", strength, weakness);
    }
}

