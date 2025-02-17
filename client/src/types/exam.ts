import { Subject } from './member';

export interface Exam {
  examId: string;
  examName: string;
  date: string;
  subjects: Subject[];
}

export interface ExamDetail {
  problemId: string;
  problem: string;
  options: string[];
  subject: string;
  problemType: 'MULTIPLE_CHOICE' | 'SHORT_ANSWER';
  chosenAnswer: string;
  answer: string;
  answerStatus: boolean;
  bookmarkStatus: boolean;
  explanation: string;
  correctRate: number;
}

export type Level = 'HARD' | 'MEDIUM' | 'EASY';

// 모의고사 생성 요청
export type ExamCreateRequest = Pick<Exam, 'subjects'> & { difficultyLevel: Level };

// 모의고사 문제 조회 응답 타입
export type ExamDetailsResponse = Pick<ExamDetail, 'problemId' | 'problem' | 'options' | 'problemType' | 'subject'>[];

// 모의고사 채점 요청 타입
export type ExamResultRequest = {
  problemAndChosenAnswers: Pick<ExamDetail, 'problemId' | 'chosenAnswer'>[];
};

// 모의고사 채점 결과 조회 응답 타입
export type ExamResultResponse = {
  round: string;
  score: number;
  examResultDetailDtos: ExamDetail[];
};
