package com.pullup.external.fcm.domain;

public record NotificationMessage(
        String title,
        String body
) {
    public static NotificationMessage of() {
        return new NotificationMessage("하루 1문제, 당신의 실력이 됩니다!", "매일 한 문제씩! 작은 습관이 큰 차이를 만듭니다. 오늘의 문제를 확인하세요.");
    }
}