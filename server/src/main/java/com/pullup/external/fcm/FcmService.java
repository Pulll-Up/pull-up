package com.pullup.external.fcm;

import static com.google.firebase.messaging.MessagingErrorCode.SENDER_ID_MISMATCH;
import static com.google.firebase.messaging.MessagingErrorCode.THIRD_PARTY_AUTH_ERROR;
import static com.google.firebase.messaging.MessagingErrorCode.UNREGISTERED;
import static com.google.rpc.Code.INVALID_ARGUMENT;
import static com.pullup.common.exception.ErrorMessage.ERR_FCM_FAILED_TO_SEND;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.pullup.common.exception.InternalServerException;
import com.pullup.member.domain.DeviceToken;
import com.pullup.member.repository.DeviceTokenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final DeviceTokenRepository deviceTokenRepository;

    @Retryable(
            retryFor = {FirebaseMessagingException.class},
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendNotifications() {
        List<DeviceToken> deviceTokens = deviceTokenRepository.findAll();

        deviceTokens.forEach(deviceToken -> {
            try {
                sendFcmMessage(deviceToken.getToken(), "하루 1문제, 당신의 실력이 됩니다!",
                        "매일 한 문제씩! 작은 습관이 큰 차이를 만듭니다. 오늘의 문제를 확인하세요.");
            } catch (FirebaseMessagingException e) {
                MessagingErrorCode errorCode = e.getMessagingErrorCode();
                log.info("errorCode: {}", errorCode);
                throw new InternalServerException(ERR_FCM_FAILED_TO_SEND);
            }
        });
    }

    private void sendFcmMessage(String deviceToken, String title, String body) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(deviceToken)
                .build();

        firebaseMessaging.send(message);
    }
}
