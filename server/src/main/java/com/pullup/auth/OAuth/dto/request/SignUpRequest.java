package com.pullup.auth.OAuth.dto.request;

import java.util.List;

public record SignUpRequest(
        List<String> subjectNames
) {
}