export interface Interview {
  interviewId: string;
  interviewAnswerId: string;
  question: string;
  memberAnswer: string;
  keywords: string[];
  createdAt: string;
  strength: string;
  weakness: string;
  answer: string;
}

export interface InterviewAnswer {
  interviewAnswerId: string;
  question: string;
  keywords: string[];
  memberName: string;
  answer: string;
  createdAt: string;
  isLiked: boolean;
  likeCount: number;
  commentCount: number;
}
