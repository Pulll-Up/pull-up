import { Subject } from '@/types/member';
import Card from './card';
import Profile from './profile';
import { convertSubject } from '@/utils/convertSubject';
import useSideBarCard from '@/hooks/useSideBarCard';
import useResponsive from '@/hooks/useResponsive';
import { Button } from '@/components/ui/button';

interface SideBarProps {
  image: string;
  name: string;
  email: string;
  subjects: Subject[];
}

const SideBar = ({ image, name, email, subjects }: SideBarProps) => {
  const { recentExamList, wrongProblemList, archiveProblemList } = useSideBarCard(5);
  const { isTabletLg } = useResponsive();

  return (
    <>
      {isTabletLg && (
        <div className="flex w-full items-center justify-between rounded-md bg-primary-50 p-2 text-xs">
          <span>오늘의 문제를 알림으로 받을래요</span>
          <Button size="fit" className="px-2 py-1 text-[10px]">
            알림 받기
          </Button>
        </div>
      )}
      <div className="flex flex-row gap-2 rounded-2xl bg-white p-4 shadow-sm sm:w-full sm:gap-6 lg:w-[330px] lg:flex-col lg:gap-1">
        <Profile image={image} name={name} email={email} subjects={convertSubject(subjects)} />
        {!isTabletLg && (
          <div className="flex w-full items-center justify-between rounded-md bg-primary-50 p-2 text-xs lg:mb-[4px]">
            <span>오늘의 문제를 알림으로 받을래요</span>
            <Button size="fit" className="px-2 py-1 text-[10px]">
              알림 받기
            </Button>
          </div>
        )}

        <div className="flex w-full flex-row gap-3 lg:flex-col lg:gap-1">
          <Card link="recent" title="최근 모의고사" dataList={recentExamList} />
          <Card link="wrong" title="내가 틀린 문제" dataList={wrongProblemList} />
          <Card link="archive" title="아카이브" dataList={archiveProblemList} />
        </div>
      </div>
    </>
  );
};

export default SideBar;
