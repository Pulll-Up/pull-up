package com.pullup.common.config;

import com.pullup.common.converter.DecryptedIdConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final DecryptedIdConverter decryptedIdConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(decryptedIdConverter);
    }
}
