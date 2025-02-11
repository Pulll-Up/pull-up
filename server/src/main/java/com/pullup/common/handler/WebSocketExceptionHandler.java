package com.pullup.common.handler;

import com.pullup.common.exception.FailResponse;
import com.pullup.common.exception.PullUpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(PullUpException.class)
    @SendTo("/topic/game/{roomId}/result") // 기존 토픽을 그대로 사용
    public FailResponse handleWebSocketPullUpException(PullUpException exception) {
        log.warn("[WebSocket PullUpException] {}: {}", exception.getClass().getName(), exception.getErrorMessage());
        return FailResponse.fail(exception.getStatus().value(), exception.getErrorMessage().getMessage());
    }

    @MessageExceptionHandler(Exception.class) // 모든 예외 처리
    @SendTo("/topic/game/{roomId}/result") // 기존 토픽을 그대로 사용
    public FailResponse handleGeneralWebSocketException(Exception exception) {
        log.error("[WebSocket Exception] {}: {}", exception.getClass().getName(), exception.getMessage(), exception);
        return FailResponse.fail(500, "서버 내부 오류가 발생했습니다.");
    }
}
