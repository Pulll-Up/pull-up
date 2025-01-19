package com.pullup.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "PullUp API",
                description = "PullUp API 명세서",
                version = "v1"
        )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi Version1OpenApi() {
        return GroupedOpenApi.builder()
                .group("PullUp API v1")
                .pathsToMatch("/**")
                .build();
    }
}

