package com.pullup.external.fcm.service;

import static com.pullup.common.exception.ErrorMessage.ERR_FCM_FAILED_TO_SEND;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.pullup.common.exception.InternalServerException;
import com.pullup.external.fcm.domain.DeviceToken;
import com.pullup.external.fcm.domain.NotificationMessage;
import com.pullup.external.fcm.repository.DeviceTokenRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    public void sendNotifications() {
        List<DeviceToken> deviceTokens = deviceTokenRepository.findAll();

        if (deviceTokens.isEmpty()) {
            return;
        }

        NotificationMessage notificationMessage = NotificationMessage.of();

        MulticastMessage multicastMessage = buildMulticastMessage(deviceTokens, notificationMessage);

        try {
            BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(multicastMessage);
            handleBatchResponse(batchResponse, deviceTokens);
        } catch (FirebaseMessagingException e) {
            log.warn("Failed to send FCM message: {}", e.getMessage());
            throw new InternalServerException(ERR_FCM_FAILED_TO_SEND);
        }
    }

    private MulticastMessage buildMulticastMessage(List<DeviceToken> deviceTokens, NotificationMessage message) {
        return MulticastMessage.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(message.title())
                                .setBody(message.body())
                                .build()
                )
                .addAllTokens(deviceTokens.stream().map(DeviceToken::getToken).toList())
                .build();
    }

    private void handleBatchResponse(BatchResponse response, List<DeviceToken> deviceTokens) {
        List<DeviceToken> invalidPushTokens = new ArrayList<>();

        List<SendResponse> responses = response.getResponses();

        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).isSuccessful()) {
                continue;
            }

            SendResponse failedResponse = responses.get(i);

            FirebaseMessagingException exception = failedResponse.getException();
            log.error("Failed to send message to token {}: {}",
                    deviceTokens.get(i).getToken(),
                    exception.getMessage());

            MessagingErrorCode errorCode = exception.getMessagingErrorCode();
            if (errorCode == MessagingErrorCode.INVALID_ARGUMENT || errorCode == MessagingErrorCode.UNREGISTERED) {
                invalidPushTokens.add(deviceTokens.get(i));
            }
        }

        log.info("Successfully sent messages: {}/{}",
                response.getSuccessCount(),
                response.getFailureCount() + response.getSuccessCount());

        if (!invalidPushTokens.isEmpty()) {
            int deletedCount = deviceTokenRepository.deleteByToken(
                    invalidPushTokens.stream().map(DeviceToken::getToken).toList());
            log.info("Deleted {} invalid push tokens", deletedCount);
        }
    }
}
