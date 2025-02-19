import { Subject } from './member';

export interface Score {
  round: string;
  score: number;
}

export interface CorrectRate {
  subject: Subject;
  correctRate: number;
}

export interface WinningRate {
  winCount: number;
  loseCount: number;
  drawCount: number;
}

export interface Streak {
  count: number;
  date: string;
  level: number;
}
