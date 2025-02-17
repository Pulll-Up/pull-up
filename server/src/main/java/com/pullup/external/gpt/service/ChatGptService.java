package com.pullup.external.gpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pullup.common.exception.ErrorMessage;
import com.pullup.common.exception.InternalServerException;
import com.pullup.external.gpt.dto.Message;
import com.pullup.external.gpt.dto.request.ChatGptRequest;
import com.pullup.external.gpt.dto.response.ChatGptResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatGptResponse analyzeAnswer(String prompt) {
        ChatGptRequest request = ChatGptRequest.of(
                "gpt-3.5-turbo",
                0.7,
                List.of(
                        Message.of("system", "너는 인터뷰 분석 AI야."),
                        Message.of("user", prompt)
                )
        );

        String rawResponse = webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parseGptResponse(rawResponse);
    }

    private ChatGptResponse parseGptResponse(String rawResponse) {
        try {
            System.out.println("GPT Raw Response: " + rawResponse);
            JsonNode rootNode = objectMapper.readTree(rawResponse);
            JsonNode choicesNode = rootNode.path("choices");

            if (choicesNode.isArray() && choicesNode.size() > 0) {
                String content = choicesNode.get(0).path("message").path("content").asText();
                JsonNode contentJson = objectMapper.readTree(content);

                String strength = extractTextFromJson(contentJson.path("strength"));
                String weakness = extractTextFromJson(contentJson.path("weakness"));

                return new ChatGptResponse(strength, weakness);
            }
        } catch (Exception e) {
            throw new InternalServerException(ErrorMessage.ERR_PARSING_GPT_RESPONSE_FAIL);
        }
        return new ChatGptResponse("강점 분석 실패", "약점 분석 실패");
    }

    private String extractTextFromJson(JsonNode node) {
        if (node.isArray()) {
            return StreamSupport.stream(node.spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.joining(" "));
        }
        return node.asText();
    }


}

