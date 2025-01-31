package com.pullup.interview.controller;

import com.pullup.interview.dto.response.InterviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Interview", description = "오늘의 문제 관련 API")
public interface InterviewApi {

    @Operation(
            summary = "오늘의 문제 조회",
            description = "오늘의 문제를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "오늘의 문제 조회 성공",
                            content = @Content(schema = @Schema(implementation = InterviewResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "오늘의 문제가 없음",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    public ResponseEntity<InterviewResponse> getTodayInterview(@PathVariable Long interviewId);

}
