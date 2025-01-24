package com.pullup.auth.OAuth.dto.response;

public record LoginResponse(
        boolean isSignedUp,
        boolean isSolvedToday
) {
}