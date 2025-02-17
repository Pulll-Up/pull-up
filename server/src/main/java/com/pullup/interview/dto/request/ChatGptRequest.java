package com.pullup.interview.dto.request;

import com.pullup.interview.dto.Message;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatGptRequest(
        String model,
        double temperature,
        List<Message> messages
) {

    public static ChatGptRequest of(String model, double temperature, List<Message> messages) {
        return ChatGptRequest.builder()
                .model(model)
                .temperature(temperature)
                .messages(messages)
                .build();
    }
}

