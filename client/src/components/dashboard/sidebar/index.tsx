import { Subject } from '@/types/member';
import Card from './card';
import Profile from './profile';
import { convertSubject } from '@/utils/convertSubject';
import useSideBarCard from '@/hooks/useSideBarCard';

interface SideBarProps {
  image: string;
  name: string;
  email: string;
  subjects: Subject[];
}

const SideBar = ({ image, name, email, subjects }: SideBarProps) => {
  const { recentExamList, wrongProblemList, archiveProblemList } = useSideBarCard(5);

  return (
    <div className="flex flex-row gap-3 rounded-2xl bg-white p-5 shadow-sm sm:w-full sm:gap-6 lg:w-[351px] lg:flex-col">
      <Profile image={image} name={name} email={email} subjects={convertSubject(subjects)} />
      <hr className="hidden border-2 border-stone-200 lg:block" />
      <div className="flex w-full flex-row gap-4 lg:flex-col">
        <Card link="/dashboard/recent" title="최근에 푼 모의고사" data={recentExamList[0]} />
        <Card link="/dashboard/wrong" title="내가 틀린 문제" data={wrongProblemList[0]} />
        <Card link="/dashboard/archive" title="아카이브" data={archiveProblemList[0]} />
      </div>
    </div>
  );
};

export default SideBar;
