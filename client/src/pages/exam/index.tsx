import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogDescription,
} from '@/components/ui/alert-dialog';
import CsConditionSelector from '@/components/common/csConditionSelector';
import exam1 from '/assets/images/exam1.png';
import { postExam } from '@/api/exam';
import { Subject } from '@/types/member';
import { Level } from '@/types/exam';

const ExamPage = () => {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false); // 모달 상태
  const [confirmHandler, setConfirmHandler] = useState<() => void>(() => () => {});

  // 모의고사 생성 함수
  const handleExamStart = async (level: Level | null, subjects: Subject[]) => {
    try {
      const requestBody = {
        difficultyLevel: level ?? 'EASY',
        subjects: subjects,
      };
      const response = await postExam(requestBody);
      navigate(`/exam/${response.examId}`);
    } catch (error) {
      console.error('모의고사 생성 실패: ', error);
    }
  };

  // 모달 열기
  const handleOpenModal = (level: Level | null, subjects: Subject[]) => {
    setOpen(true);
    setConfirmHandler(() => () => handleExamStart(level, subjects));
  };

  // 확인 시 실행할 함수 상태

  return (
    <>
      <div className="flex h-full w-full items-center justify-around bg-Main px-16 py-10">
        <div className="mt-[94px] flex w-full items-center justify-around sm:mt-16">
          <CsConditionSelector
            title="시험 분야 선택"
            text="모의고사 만들기"
            onClick={handleOpenModal} // 버튼 클릭 시 모달 열기
            isExam={true}
          />
          <img src={exam1} alt="exam1" className="hidden h-auto w-[400px] lg:block xl:w-[600px]" />
        </div>
      </div>

      {/* AlertDialog 모달 */}
      <AlertDialog open={open} onOpenChange={setOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>시험을 시작하시겠습니까?</AlertDialogTitle>
            <AlertDialogDescription>
              시험을 시작하면 제한 시간 동안 문제를 풀어야 하며, 페이지를 벗어나거나 새로고침을 할 경우 진행 중인 시험은
              0점 처리 됩니다.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel onClick={() => setOpen(false)}>취소</AlertDialogCancel>
            <AlertDialogAction onClick={confirmHandler}>시험 시작</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
};

export default ExamPage;
