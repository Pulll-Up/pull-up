import { useGetMember } from '@/api/member';
import SideBar from '@/components/dashboard/sidebar';
import useResponsive from '@/hooks/useResponsive';
import { registerServiceWorker, requestPermission } from '@/utils/serviceWorker';
import { throttle } from 'lodash';
import { lazy } from 'react';
import { Outlet } from 'react-router-dom';

const MobileTopBar = lazy(() => import('@/components/dashboard/sidebar/MobileTopBar'));

const DashBoardLayout = () => {
  const { isMobile, isTabletMd } = useResponsive();

  const { data: member } = useGetMember();

  if (!member) return null;

  const onClick = throttle(
    async () => {
      await registerServiceWorker();
      await requestPermission();
    },
    3000,
    { leading: true, trailing: false },
  );

  return (
    <div className="box-border flex min-h-screen bg-Main pt-[94px] sm:pt-16">
      {isMobile || isTabletMd ? (
        <div className="flex flex-col gap-5 p-2 py-4 sm:p-4">
          <MobileTopBar
            image={member.profileImageUrl}
            name={member.name}
            email={member.email}
            subjects={member.interestSubjects}
            onClick={onClick}
          />
          <Outlet />
        </div>
      ) : (
        <div className="box-border flex flex-1 flex-col gap-4 p-6 md:flex-col-reverse lg:flex-row">
          <main className="flex h-full flex-1 overflow-y-auto">
            <Outlet />
          </main>
          <SideBar
            image={member.profileImageUrl}
            name={member.name}
            email={member.email}
            subjects={member.interestSubjects}
            onClick={onClick}
          />
        </div>
      )}
    </div>
  );
};

export default DashBoardLayout;
