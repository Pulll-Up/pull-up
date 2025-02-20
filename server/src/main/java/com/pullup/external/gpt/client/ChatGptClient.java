package com.pullup.external.gpt.client;

import com.pullup.external.gpt.dto.request.ChatGptRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "chatGptClient", url = "https://api.openai.com/v1")
public interface ChatGptClient {
    @PostMapping("/chat/completions")
    String getPrediction(@RequestBody ChatGptRequest request,
                         @RequestHeader("Authorization") String authorization);
}
