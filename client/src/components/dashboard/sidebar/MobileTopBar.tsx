import SwipeCard from '@/components/common/swipeCard';
import MobileProfile from './profile/MobileProfile';
import Card from './card';
import { Subject } from '@/types/member';
import { convertSubject } from '@/utils/convertSubject';
import useSideBarCard from '@/hooks/useSideBarCard';

interface MobileTopBarProps {
  image: string;
  name: string;
  email: string;
  subjects: Subject[];
}

const MobileTopBar = ({ image, name, email, subjects }: MobileTopBarProps) => {
  const { recentExamList, wrongProblemList, archiveProblemList } = useSideBarCard(5);

  const examComponents = [
    { id: 'recent', component: <Card link="recent" title="최근에 푼 모의고사" dataList={recentExamList} /> },
    { id: 'wrong', component: <Card link="wrong" title="내가 틀린 문제" dataList={wrongProblemList} /> },
    { id: 'archive', component: <Card link="archive" title="아카이브" dataList={archiveProblemList} /> },
  ];

  return (
    <div className="flex flex-col gap-4">
      <MobileProfile image={image} name={name} email={email} subjects={convertSubject(subjects)} />
      <SwipeCard components={examComponents} dots={true} />
    </div>
  );
};

export default MobileTopBar;
