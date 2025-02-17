self.importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
self.importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

// install과 activate 핸들러에 실제 콜백 함수 추가
self.addEventListener('install', function (event) {
  event.waitUntil(self.skipWaiting());
});

self.addEventListener('activate', function (event) {
  event.waitUntil(self.clients.claim());
});

self.addEventListener('pushsubscriptionchange', function (event) {
  // 구독 변경 처리
});

self.addEventListener('push', function (e) {
  if (!e.data.json()) return;

  const resultData = e.data.json().notification;
  const notificationTitle = resultData.title;
  const notificationOptions = {
    body: resultData.body,
    icon: resultData.image,
    tag: resultData.tag,
    ...resultData,
  };

  e.waitUntil(self.registration.showNotification(notificationTitle, notificationOptions));
});

self.addEventListener('notificationclick', function (event) {
  const url = 'https://www.pull-up.store/signin';
  event.notification.close();
  event.waitUntil(self.clients.openWindow(url));
});

// Firebase 초기화를 더 일찍 수행
let messaging = null;

self.addEventListener('message', function (event) {
  if (event.data && event.data.type === 'FIREBASE_CONFIG') {
    // Firebase가 아직 초기화되지 않은 경우에만 초기화
    if (!messaging) {
      self.firebase.initializeApp(event.data.config);
      messaging = self.firebase.messaging();

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
  }
});
