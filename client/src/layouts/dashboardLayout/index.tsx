import { useGetMemberInfo } from '@/api/member';
import SideBar from '@/components/dashboard/sidebar';
import MobileTopBar from '@/components/dashboard/sidebar/MobileTopBar';
import useResponsive from '@/hooks/useResponsive';
import { memberStore } from '@/stores/memberStore';
import { useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const DashBoardLayout = () => {
  const { isMobile, isTabletMd } = useResponsive();
  const navigate = useNavigate();
  const { data: member, isLoading } = useGetMemberInfo();
  const { isLoggedIn } = memberStore();

  useEffect(() => {
    if (!isLoading && !isLoggedIn) {
      toast.error('로그인이 필요합니다.', { position: 'bottom-center' });
      navigate('/signin');
      return;
    }
  }, [member, isLoading]);

  return (
    <div className="flex min-h-screen bg-Main pt-[94px] sm:pt-16">
      {isMobile || isTabletMd ? (
        <div className="flex flex-col gap-5 p-6">
          <MobileTopBar
            image={member?.profileImageUrl ?? ''}
            name={member?.name ?? ''}
            email={member?.email ?? ''}
            subjects={member?.interestSubjects ?? []}
          />
          <Outlet />
        </div>
      ) : (
        <div className="box-border flex flex-1 flex-col gap-4 p-8 md:flex-col-reverse lg:flex-row">
          <main className="flex h-full flex-1 overflow-y-auto">
            <Outlet />
          </main>
          <SideBar
            image={member?.profileImageUrl ?? ''}
            name={member?.name ?? ''}
            email={member?.email ?? ''}
            subjects={member?.interestSubjects ?? []}
          />
        </div>
      )}
    </div>
  );
};

export default DashBoardLayout;
