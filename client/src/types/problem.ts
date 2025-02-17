import { Subject } from './member';

export interface Problem {
  problemId: string;
  question: string;
  subject: Subject;
  date: string;
}

export interface ProblemDetail {
  question: string;
  options: string[];
  answer: string;
  explanation: string;
  correctRate: number;
  subject: string;
  bookmarkStatus: boolean;
}

export type ProblemBrief = Omit<Problem, 'date'>;
