package com.pullup.external.gpt.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.pullup.external.gpt.client")
public class ChatGptFeignConfig {
}
