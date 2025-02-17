self.importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
self.importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

let messaging = null;

// Firebase 초기화 및 메시징 설정을 담당하는 함수
function initializeFirebase(config) {
  if (!messaging) {
    self.firebase.initializeApp(config);
    messaging = self.firebase.messaging();
  }
  return messaging;
}

// 초기 이벤트 리스너 등록
self.addEventListener('install', function (event) {
  event.waitUntil(self.skipWaiting());
});

self.addEventListener('activate', function (event) {
  event.waitUntil(self.clients.claim());
});

self.addEventListener('push', function (event) {
  if (!event.data) return;

  const data = event.data.json();
  if (!data.notification) return;

  const notificationTitle = data.notification.title;
  const notificationOptions = {
    body: data.notification.body,
    icon: data.notification.image,
    tag: data.notification.tag,
    ...data.notification,
  };

  event.waitUntil(self.registration.showNotification(notificationTitle, notificationOptions));
});

self.addEventListener('pushsubscriptionchange', function (event) {
  // 구독 변경 처리 로직
  event.waitUntil(
    // 필요한 재구독 로직 구현
    Promise.resolve(),
  );
});

self.addEventListener('notificationclick', function (event) {
  const url = 'https://www.pull-up.store/signin';
  event.notification.close();
  event.waitUntil(self.clients.openWindow(url));
});

// Firebase 설정을 받는 메시지 핸들러
self.addEventListener('message', function (event) {
  if (event.data && event.data.type === 'FIREBASE_CONFIG') {
    const messaging = initializeFirebase(event.data.config);

    messaging.onBackgroundMessage(function (payload) {
      const notificationTitle = payload.notification.title;
      const notificationOptions = {
        body: payload.notification.body,
        icon: payload.notification.image || '/favicon.png',
        badge: '/favicon.png',
        tag: payload.notification.tag,
        data: payload.data,
        requireInteraction: true,
        vibrate: [200, 100, 200],
        actions: payload.notification.actions || [],
        renotify: true,
        click_action: payload.notification.click_action || 'FLUTTER_NOTIFICATION_CLICK',
      };

      return self.registration.showNotification(notificationTitle, notificationOptions);
    });
  }
});
