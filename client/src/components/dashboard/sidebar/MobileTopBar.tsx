import SwipeCard from '@/components/common/swipeCard';
import MobileProfile from './profile/MobileProfile';
import Card from './card';
import { Subject } from '@/types/member';
import { convertSubject } from '@/utils/convertSubject';
import useSideBarCard from '@/hooks/useSideBarCard';
import { Button } from '@/components/ui/button';
import { ButtonMouseEvent } from '@/types/event';

interface MobileTopBarProps {
  image: string;
  name: string;
  email: string;
  subjects: Subject[];
  onClick: (e: ButtonMouseEvent) => void;
}

const MobileTopBar = ({ image, name, email, subjects, onClick }: MobileTopBarProps) => {
  const { recentExamList, wrongProblemList, archiveProblemList } = useSideBarCard(5);

  const examComponents = [
    { id: 'recent', component: <Card link="recent" title="최근에 푼 모의고사" dataList={recentExamList} /> },
    { id: 'wrong', component: <Card link="wrong" title="내가 틀린 문제" dataList={wrongProblemList} /> },
    { id: 'archive', component: <Card link="archive" title="아카이브" dataList={archiveProblemList} /> },
  ];

  return (
    <div className="flex flex-col gap-4">
      <MobileProfile image={image} name={name} email={email} subjects={convertSubject(subjects)} />
      <div className="flex w-full items-center justify-between rounded-md bg-primary-50 p-2 text-xs">
        <span>오늘의 문제를 알림으로 받을래요</span>
        <Button size="fit" className="px-2 py-1 text-[10px]" onClick={onClick}>
          알림 받기
        </Button>
      </div>
      <SwipeCard components={examComponents} dots={true} />
    </div>
  );
};

export default MobileTopBar;
