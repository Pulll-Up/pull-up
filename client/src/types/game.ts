export type RoomStatus = 'WAITING' | 'PLAYING' | 'FINISHED';
export type ResultStatus = 'WIN' | 'DRAW' | 'LOSE';

export type PlayerType = 'player1P' | 'player2P';

export interface Card {
  type: 'question' | 'answer';
  disabled: boolean;
  content: string;
}

export interface Player {
  memberId: number;
  name: string;
  score: number;
}

export interface PlayerResult {
  name: string;
  score: number;
  status: ResultStatus;
}

export interface StompRoomInfo {
  roomId: string;
  gameRoomStatus: RoomStatus;
  player1P: Player;
  player2P: Player;
  problemCardWithoutCardIds: Card[];
}

export interface StompGameResult {
  isDraw: boolean;
  isForfeit: boolean;
  player1P: PlayerResult;
  player2P: PlayerResult;
}

export interface SubjectSelect {
  algorithm: boolean;
  computerArchitecture: boolean;
  database: boolean;
  dataStructure: boolean;
  network: boolean;
  operatingSystem: boolean;
}
