import { http, HttpResponse } from 'msw';
import { ExamCreateRequest } from '@/types/exam';

export const examHandler = [
  // 모의고사 문제 생성
  http.post('http://localhost:8080/api/v1/exam/me', async ({ request }) => {
    const req = (await request.json()) as ExamCreateRequest;

    if (!req.subjects || !req.difficultyLevel) {
      return HttpResponse.json({ message: 'subjects와 difficultyLevel이 필요합니다.' }, { status: 400 });
    }

    const responseData = { examId: 1 };
    return HttpResponse.json(responseData, { status: 201 });
  }),

  // 모의고사 문제 조회
  http.get('http://localhost:8080/api/v1/exam/:examId', async ({ params }) => {
    const { examId } = params;
    if (examId) {
      // 성공 응답
      return HttpResponse.json(
        {
          examDetailsDtos: [
            {
              problemId: 1,
              problem: 'CPU의 주요 구성 요소가 아닌 것은 무엇입니까?',
              options: ['ALU', 'CU', 'RAM', '레지스터'],
              subject: 'COMPUTER_ARCHITECTURE',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 2,
              problem: '다음 중 프로세스와 스레드의 차이점으로 올바른 설명은 무엇입니까?',
              options: [
                '프로세스는 메모리를 공유하지 않는다.',
                '스레드는 독립적으로 실행된다.',
                '프로세스는 동일한 자원을 공유한다.',
                '스레드는 각기 다른 메모리 공간을 가진다.',
              ],
              subject: 'OPERATING_SYSTEM',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 3,
              problem: 'TCP와 UDP의 차이점 중 TCP가 제공하는 주요 기능은 무엇입니까?',
              options: [],
              subject: 'NETWORK',
              problemType: 'SHORT_ANSWER',
            },
            // {
            //   problemId: 4,
            //   problem: '데이터베이스에서 정규화의 주요 목적은 무엇입니까?',
            //   options: ['데이터 중복 제거', '빠른 검색 속도', '메모리 사용 최소화', '클러스터링'],
            //   subject: 'DATABASE',
            //   problemType: 'MULTIPLE_CHOICE',
            // },
            // {
            //   problemId: 5,
            //   problem: '다음 중 이진 탐색(Binary Search)에 대한 설명으로 올바른 것은 무엇입니까?',
            //   options: [
            //     '정렬되지 않은 배열에 사용된다.',
            //     '시간 복잡도는 O(n)이다.',
            //     '정렬된 배열에 사용된다.',
            //     '탐색 범위를 무작위로 선택한다.',
            //   ],
            //   subject: 'ALGORITHM',
            //   problemType: 'MULTIPLE_CHOICE',
            // },
            // {
            //   problemId: 6,
            //   problem: '스택의 주요 특징은 무엇입니까?',
            //   options: ['FIFO', 'LIFO', '랜덤 접근', '순차 접근'],
            //   subject: 'DATA_STRUCTURE',
            //   problemType: 'MULTIPLE_CHOICE',
            // },
            // {
            //   problemId: 7,
            //   problem: 'CPU가 명령어를 실행할 때 첫 번째 단계는 무엇입니까?',
            //   options: [],
            //   subject: 'COMPUTER_ARCHITECTURE',
            //   problemType: 'SHORT_ANSWER',
            // },
            // {
            //   problemId: 8,
            //   problem: '운영체제의 메모리 관리 기법 중 페이징(Paging)의 특징은 무엇입니까?',
            //   options: [
            //     '고정 크기의 페이지로 분할한다.',
            //     '가변 크기의 메모리 블록을 사용한다.',
            //     '외부 단편화가 발생한다.',
            //     '프로세스 간 메모리를 직접 공유한다.',
            //   ],
            //   subject: 'OPERATING_SYSTEM',
            //   problemType: 'MULTIPLE_CHOICE',
            // },
            // {
            //   problemId: 9,
            //   problem: "데이터베이스 트랜잭션의 ACID 속성 중 'Atomicity'의 의미는 무엇입니까?",
            //   options: [
            //     '모든 작업이 성공하거나 실패해야 한다.',
            //     '동일한 트랜잭션에서 항상 같은 결과를 반환한다.',
            //     '다른 트랜잭션이 영향을 주지 않는다.',
            //     '작업이 영구적으로 유지된다.',
            //   ],
            //   subject: 'DATABASE',
            //   problemType: 'MULTIPLE_CHOICE',
            // },
            // {
            //   problemId: 10,
            //   problem: '다익스트라 알고리즘(Dijkstra Algorithm)의 주요 용도는 무엇입니까?',
            //   options: ['최소 신장 트리 구성', '최단 경로 탐색', '그래프 순회', '이진 탐색'],
            //   subject: 'ALGORITHM',
            //   problemType: 'MULTIPLE_CHOICE',
            // },
          ],
        },
        { status: 200 },
      );
    }
    return HttpResponse.json({ status: 404 });
  }),

  // 모의고사 채점
  http.post('http://localhost:8080/api/v1/exam/:examId', async ({ params, request }) => {
    const { examId } = params;
    const requestBody = (await request.json()) as {
      problemAndChosenAnswers: { problemId: number; chosenAnswer: string }[];
    };

    return HttpResponse.json(
      {
        message: '답안 제출 성공',
        examId,
        submittedAnswers: requestBody,
      },
      { status: 200 },
    );
  }),

  // 모의고사 채점 결과 조회
  http.get('http://localhost:8080/api/v1/exam/:examId/result', async ({ params }) => {
    const { examId } = params;
    if (examId) {
      // 성공 응답
      return HttpResponse.json(
        {
          round: '제 3회 모의고사',
          score: 70,
          examResultDetailDtos: [
            {
              problemId: 1,
              problem: 'CPU의 주요 구성 요소가 아닌 것은 무엇입니까?',
              options: ['ALU', 'CU', 'RAM', '레지스터'],
              chosenAnswer: 'RAM',
              answer: 'RAM',
              answerStatus: true,
              bookmarkStatus: true,
              explanation: 'RAM은 메인 메모리로, CPU가 직접 연산에 사용하는 구성 요소가 아닙니다.',
              correctRate: 83,
              subject: 'COMPUTER_ARCHITECTURE',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 2,
              problem: '다음 중 프로세스와 스레드의 차이점으로 올바른 설명은 무엇입니까?',
              options: [
                '프로세스는 메모리를 공유하지 않는다.',
                '스레드는 독립적으로 실행된다.',
                '프로세스는 동일한 자원을 공유한다.',
                '스레드는 각기 다른 메모리 공간을 가진다.',
              ],
              chosenAnswer: '스레드는 각기 다른 메모리 공간을 가진다.',
              answer: '프로세스는 메모리를 공유하지 않는다.',
              answerStatus: false,
              bookmarkStatus: false,
              explanation:
                '프로세스는 각기 다른 메모리 공간을 사용하지만, 스레드는 동일한 프로세스 내에서 자원을 공유합니다.',
              correctRate: 82,
              subject: 'OPERATING_SYSTEM',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 3,
              problem: 'TCP와 UDP의 차이점 중 TCP가 제공하는 주요 기능은 무엇입니까?',
              options: [],
              chosenAnswer: '신뢰성',
              answer: '신뢰성',
              answerStatus: true,
              bookmarkStatus: true,
              explanation:
                'TCP는 데이터의 신뢰성을 보장하기 위해 연결을 설정하는 반면, UDP는 빠른 전송을 위해 비연결 기반으로 동작합니다.',
              correctRate: 90,
              subject: 'NETWORK',
              problemType: 'SHORT_ANSWER',
            },
            {
              problemId: 4,
              problem: '데이터베이스에서 정규화의 주요 목적은 무엇입니까?',
              options: ['데이터 중복 제거', '빠른 검색 속도', '메모리 사용 최소화', '클러스터링'],
              chosenAnswer: '데이터 중복 제거',
              answer: '데이터 중복 제거',
              answerStatus: true,
              bookmarkStatus: true,
              explanation:
                '정규화는 데이터베이스에서 중복 데이터를 제거하고 데이터 무결성을 유지하기 위한 설계 기법입니다.',
              correctRate: 85,
              subject: 'DATABASE',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 5,
              problem: '다음 중 이진 탐색(Binary Search)에 대한 설명으로 올바른 것은 무엇입니까?',
              options: [
                '정렬되지 않은 배열에 사용된다.',
                '시간 복잡도는 O(n)이다.',
                '정렬된 배열에 사용된다.',
                '탐색 범위를 무작위로 선택한다.',
              ],
              chosenAnswer: '시간 복잡도는 O(n)이다.',
              answer: '정렬된 배열에 사용된다.',
              answerStatus: false,
              bookmarkStatus: false,
              explanation:
                '이진 탐색은 정렬된 배열에 대해 탐색 범위를 절반으로 줄여가며 빠르게 값을 찾는 알고리즘입니다.',
              correctRate: 88,
              subject: 'ALGORITHM',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 6,
              problem: '스택의 주요 특징은 무엇입니까?',
              options: ['FIFO', 'LIFO', '랜덤 접근', '순차 접근'],
              chosenAnswer: 'FIFO',
              answer: 'LIFO',
              answerStatus: false,
              bookmarkStatus: false,
              explanation:
                '스택(Stack)은 마지막에 삽입된 데이터가 가장 먼저 제거되는 LIFO(Last In First Out) 방식의 자료구조입니다.',
              correctRate: 80,
              subject: 'DATA_STRUCTURE',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 7,
              problem: 'CPU가 명령어를 실행할 때 첫 번째 단계는 무엇입니까?',
              options: [],
              chosenAnswer: '인출',
              answer: '인출',
              answerStatus: true,
              bookmarkStatus: false,
              explanation: 'CPU는 명령어를 인출(Fetch), 해석(Decode), 실행(Execute)하는 단계를 거쳐 동작합니다.',
              correctRate: 87,
              subject: 'COMPUTER_ARCHITECTURE',
              problemType: 'SHORT_ANSWER',
            },
            {
              problemId: 8,
              problem: '운영체제의 메모리 관리 기법 중 페이징(Paging)의 특징은 무엇입니까?',
              options: [
                '고정 크기의 페이지로 분할한다.',
                '가변 크기의 메모리 블록을 사용한다.',
                '외부 단편화가 발생한다.',
                '프로세스 간 메모리를 직접 공유한다.',
              ],
              chosenAnswer: '고정 크기의 페이지로 분할한다.',
              answer: '고정 크기의 페이지로 분할한다.',
              answerStatus: true,
              bookmarkStatus: false,
              explanation:
                '페이징은 메모리를 고정 크기의 페이지로 나누어 외부 단편화를 방지하는 메모리 관리 기법입니다.',
              correctRate: 84,
              subject: 'OPERATING_SYSTEM',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 9,
              problem: "데이터베이스 트랜잭션의 ACID 속성 중 'Atomicity'의 의미는 무엇입니까?",
              options: [
                '모든 작업이 성공하거나 실패해야 한다.',
                '동일한 트랜잭션에서 항상 같은 결과를 반환한다.',
                '다른 트랜잭션이 영향을 주지 않는다.',
                '작업이 영구적으로 유지된다.',
              ],
              chosenAnswer: '모든 작업이 성공하거나 실패해야 한다.',
              answer: '모든 작업이 성공하거나 실패해야 한다.',
              answerStatus: true,
              bookmarkStatus: true,
              explanation:
                '원자성(Atomicity)은 트랜잭션 내의 모든 작업이 완전히 성공하거나, 전혀 수행되지 않은 상태로 돌아가야 함을 의미합니다.',
              correctRate: 91,
              subject: 'DATABASE',
              problemType: 'MULTIPLE_CHOICE',
            },
            {
              problemId: 10,
              problem: '다익스트라 알고리즘(Dijkstra Algorithm)의 주요 용도는 무엇입니까?',
              options: ['최소 신장 트리 구성', '최단 경로 탐색', '그래프 순회', '이진 탐색'],
              chosenAnswer: '최단 경로 탐색',
              answer: '최단 경로 탐색',
              answerStatus: true,
              bookmarkStatus: true,
              explanation:
                '다익스트라 알고리즘은 그래프에서 출발 노드로부터 다른 노드들까지의 최단 경로를 탐색하는 알고리즘입니다.',
              correctRate: 89,
              subject: 'ALGORITHM',
              problemType: 'MULTIPLE_CHOICE',
            },
          ],
        },
        { status: 200 },
      );
    }
    return HttpResponse.json({ status: 404 });
  }),

  // 최근 푼 모의고사 전체 조회
  http.get('http://localhost:8080/api/v1/exam/me/all', async ({ request }) => {
    const url = new URL(request.url);
    const page = url.searchParams.get('page') || 1;
    const size = url.searchParams.get('size') || 10;

    const data = {
      getExamResponses: [
        {
          examId: 7,
          examName: '제 3회 모의고사',
          date: '2025-02-04T19:00:00',
          subjects: ['알고리즘', '네트워크', '운영체제', '데이터베이스'],
        },
        {
          examId: 9,
          examName: '제 2회 모의고사',
          date: '2025-01-28T19:00:00',
          subjects: ['운영체제', '데이터베이스', '알고리즘', '네트워크크'],
        },
        {
          examId: 13,
          examName: '제 4회 모의고사',
          date: '2025-01-22T20:20:22.022201',
          subjects: ['운영체제', '데이터베이스'],
        },
        {
          examId: 12,
          examName: '제 5회 모의고사',
          date: '2025-01-24T20:20:22.022201',
          subjects: ['운영체제', '데이터베이스'],
        },
      ],
      pageable: {
        page: 0,
        size: 3,
        totalPages: 1,
        totalElements: 3,
      },
    };

    return HttpResponse.json(
      {
        content: data.getExamResponses,
        pageable: {
          page: page, // 1부터 시작하는 페이지 번호로 반환
          size,
          totalPages: data.pageable.totalPages,
          totalElements: data.pageable.totalElements,
        },
      },
      { status: 200 },
    );
  }),
];
