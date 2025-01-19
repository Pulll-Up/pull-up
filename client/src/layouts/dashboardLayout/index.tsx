import SideBar from '@/components/dashboard/sidebar';
import { Outlet } from 'react-router-dom';

const DashBoardLayout = () => {
  // 더미데이터
  const { image, name, email, subjects } = {
    image: 'https://avatars.githubusercontent.com/u/55848610?v=4',
    name: '강지은',
    email: 'kkang@gmail.com',
    subjects: ['운영체제', '네트워크', 'OS'],
  };

  return (
    <div className="bg-background-mainBG box-border flex flex-1 gap-4 p-8">
      <main className="flex flex-1 overflow-y-auto">
        <Outlet />
      </main>
      <SideBar image={image} name={name} email={email} subjects={subjects} />
    </div>
  );
};

export default DashBoardLayout;
