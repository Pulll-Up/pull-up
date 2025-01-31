import Icon from '@/components/common/icon';
import { InterviewListResponse } from '@/types/interview';
import MenuItem from './menuitem';

interface SideMenuProps {
  isOpen: boolean;
  interviewList: InterviewListResponse[];
  handleMenuClick: (e: React.MouseEvent<HTMLButtonElement>) => void;
  handleSearchClick: () => void;
  onInterviewClick: (interviewId: number) => void;
}

const SideMenu = ({ isOpen, interviewList, handleMenuClick, handleSearchClick, onInterviewClick }: SideMenuProps) => {
  return (
    <div
      className={`fixed left-0 top-[68px] z-30 h-full border-r border-primary-200 bg-white transition-transform duration-300 ease-in-out ${
        isOpen ? 'translate-x-0' : '-translate-x-full'
      }`}
    >
      <div className="flex h-full w-[350px] flex-col gap-9 px-6 py-10">
        <div className="flex justify-between">
          <button onClick={handleMenuClick}>
            <Icon id="menu" />
          </button>
          <button onClick={handleSearchClick}>
            <Icon id="search" />
          </button>
        </div>
        <div className="flex w-full flex-col gap-2">
          {interviewList.map((item, id) => (
            <MenuItem key={id} title={item.question} onInterviewClick={() => onInterviewClick(item.interviewId)} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default SideMenu;
