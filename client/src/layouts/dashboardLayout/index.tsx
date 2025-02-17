import { getMember } from '@/api/member';
import SideBar from '@/components/dashboard/sidebar';
import useResponsive from '@/hooks/useResponsive';
import { queryClient } from '@/main';
import { Member } from '@/types/member';
import { lazy, useEffect, useState } from 'react';
import { Outlet } from 'react-router-dom';

const MobileTopBar = lazy(() => import('@/components/dashboard/sidebar/MobileTopBar'));

const DashBoardLayout = () => {
  const { isMobile, isTabletMd } = useResponsive();
  const [member, setMember] = useState<Member>();

  useEffect(() => {
    const fetchMember = async () => {
      const data = await queryClient.fetchQuery({
        queryKey: ['member'],
        queryFn: getMember,
      });

      if (!data) return null;

      setMember(data);
    };

    fetchMember();
  }, []);

  if (!member) return null;

  return (
    <div className="flex min-h-screen bg-Main pt-[94px] sm:pt-16">
      {isMobile || isTabletMd ? (
        <div className="flex flex-col gap-5 p-2 py-4 sm:p-4">
          <MobileTopBar
            image={member.profileImageUrl}
            name={member.name}
            email={member.email}
            subjects={member.interestSubjects}
          />
          <Outlet />
        </div>
      ) : (
        <div className="box-border flex flex-1 flex-col gap-4 p-8 md:flex-col-reverse lg:flex-row">
          <main className="flex h-full flex-1 overflow-y-auto">
            <Outlet />
          </main>
          <SideBar
            image={member.profileImageUrl}
            name={member.name}
            email={member.email}
            subjects={member.interestSubjects}
          />
        </div>
      )}
    </div>
  );
};

export default DashBoardLayout;
