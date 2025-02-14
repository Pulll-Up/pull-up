package com.pullup.external.fcm.service;

import static com.pullup.common.exception.ErrorMessage.ERR_FCM_FAILED_TO_SEND;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.pullup.common.exception.InternalServerException;
import com.pullup.external.fcm.domain.NotificationMessage;
import com.pullup.external.fcm.domain.DeviceToken;
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
@Transactional(readOnly = true)
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final DeviceTokenRepository deviceTokenRepository;

    public void sendNotifications() {
        List<DeviceToken> deviceTokens = deviceTokenRepository.findAll();

        List<String> tokens = deviceTokens.stream().map(DeviceToken::getToken).toList();

        if (tokens.isEmpty()) {
            return;
        }

        NotificationMessage notificationMessage = NotificationMessage.of();

        MulticastMessage multicastMessage = buildMulticastMessage(tokens, notificationMessage);

        try {
            BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(multicastMessage);
            log.info("Successfully sent FCM message: {}", batchResponse.getSuccessCount());
            log.info("Failed to send FCM message: {}", batchResponse.getFailureCount());
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message: {}", e.getMessage());
            throw new InternalServerException(ERR_FCM_FAILED_TO_SEND);
        }
    }


    private MulticastMessage buildMulticastMessage(List<String> pushTokens, NotificationMessage message) {
        return MulticastMessage.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(message.title())
                                .setBody(message.body())
                                .build()
                )
                .addAllTokens(pushTokens)
                .build();
    }
}
