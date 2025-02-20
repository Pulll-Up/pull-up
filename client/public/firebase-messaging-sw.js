self.importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
self.importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

self.addEventListener('message', function (event) {
  if (event.data && event.data.type === 'FIREBASE_CONFIG') {
    self.firebase.initializeApp(event.data.config);
    const messaging = self.firebase.messaging();

    // 백그라운드 메시지
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

self.addEventListener('install', function () {
  self.skipWaiting();
});

self.addEventListener('activate', function () {});

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
