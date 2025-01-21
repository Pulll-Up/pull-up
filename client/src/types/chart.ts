import { Subject } from './member';

export interface Score {
  time: string;
  score: number;
}

export interface CorrectRate {
  subject: Subject;
  correctRate: number;
}

export interface WinningRate {
  winningRate: number;
}

export interface Streak {
  count: number;
  date: string;
  level: number;
}
