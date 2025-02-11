import { create } from 'zustand';
import { Client, StompSubscription } from '@stomp/stompjs';
import { RoomStatus, StompGameResult, StompRoomInfo } from '@/types/game';

interface WebSocketState {
  client: Client | null;
  roomStatus: RoomStatus;
  roomInfo: StompRoomInfo;
  gameResult: StompGameResult;
  subscriptions: Record<string, StompSubscription | null>;
  connectWebSocket: () => void;
  disconnectWebSocket: () => void;
  updateSubscription: (roomId: string, pageType: 'status' | 'game' | 'result') => void;
  sendMessage: (destination: string, payload?: unknown) => void;
}

const INITIAL_ROOMSTATUS = 'WAITING';
const INITIAL_ROOMINFO: StompRoomInfo = {
  roomId: '',
  gameRoomStatus: 'WAITING',
  player1P: { memberId: 0, name: '', score: 0 },
  player2P: { memberId: 0, name: '', score: 0 },
  problemCardWithoutCardIds: [],
};
const INITIAL_GAMERESULT: StompGameResult = {
  isDraw: true,
  isForfeit: false,
  player1P: {
    name: '',
    score: 0,
    status: 'DRAW',
  },
  player2P: {
    name: '',
    score: 0,
    status: 'DRAW',
  },
};

export const useWebSocketStore = create<WebSocketState>((set, get) => ({
  client: null,
  roomStatus: INITIAL_ROOMSTATUS,
  roomInfo: INITIAL_ROOMINFO,
  gameResult: INITIAL_GAMERESULT,
  subscriptions: {},

  connectWebSocket: () => {
    if (get().client) return;

    const client = new Client({
      webSocketFactory: () => new WebSocket(`${import.meta.env.VITE_WEBSOCKET_URL}`),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    // client.onConnect = () => {};

    // client.onStompError = (frame) => {
    //   console.log('Websocket: STOMP 에러:', frame.headers['message']);
    // };

    client.activate();
    set({ client });
  },

  disconnectWebSocket: () => {
    const { client, subscriptions } = get();

    Object.values(subscriptions).forEach((sub) => sub?.unsubscribe());

    set({
      roomStatus: INITIAL_ROOMSTATUS,
      roomInfo: INITIAL_ROOMINFO,
      gameResult: INITIAL_GAMERESULT,
    });

    if (client) {
      client.deactivate();
      set({ client: null, subscriptions: {} });
    }
  },

  updateSubscription: (roomId: string, pageType: 'status' | 'game' | 'result') => {
    const { client, subscriptions } = get();
    if (!client || !client.connected) return;

    Object.values(subscriptions).forEach((sub) => sub?.unsubscribe());

    set({
      roomStatus: INITIAL_ROOMSTATUS,
      roomInfo: INITIAL_ROOMINFO,
      gameResult: INITIAL_GAMERESULT,
    });

    const newSubscriptions: Record<string, StompSubscription> = {};

    if (pageType === 'status') {
      newSubscriptions['status'] = client.subscribe(`/topic/game/${roomId}/status`, (message) => {
        const { status } = JSON.parse(message.body);
        set({ roomStatus: status });
      });
    }

    if (pageType === 'game') {
      newSubscriptions['room'] = client.subscribe(`/topic/game/${roomId}`, (message) => {
        const { roomId, gameRoomStatus, player1P, player2P, problemCardWithoutCardIds } = JSON.parse(message.body);
        set({ roomInfo: { roomId, gameRoomStatus, player1P, player2P, problemCardWithoutCardIds } });
      });
    }

    if (pageType === 'result') {
      newSubscriptions['result'] = client.subscribe(`/topic/game/${roomId}/result`, (message) => {
        const { isDraw, isForfeit, player1P, player2P } = JSON.parse(message.body);
        set({ gameResult: { isDraw, isForfeit, player1P, player2P } });
      });
    }

    set({ subscriptions: newSubscriptions });
  },

  sendMessage: (destination, payload) => {
    const { client } = get();

    if (!client || !client.connected) {
      return;
    }

    client.publish({ destination, body: JSON.stringify(payload) });
  },
}));
