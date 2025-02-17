import Icon from '@/components/common/icon';
import MenuItem from './menuitem';
import { InterviewListResponse } from '@/types/response/interview';
import { ButtonMouseEvent } from '@/types/event';

interface SideMenuProps {
  isOpen: boolean;
  interviewList: InterviewListResponse[];
  handleMenuClick: (e: ButtonMouseEvent) => void;
  handleSearchClick: () => void;
  onInterviewClick: (interviewId: number) => void;
}

const SideMenu = ({ isOpen, interviewList, handleMenuClick, handleSearchClick, onInterviewClick }: SideMenuProps) => {
  return (
    <div
      className={`fixed left-0 top-[94px] z-30 h-full border-r border-primary-200 bg-white transition-transform duration-300 ease-in-out md:top-[68px] ${
        isOpen ? 'translate-x-0' : '-translate-x-full'
      }`}
    >
      <div className="flex h-full w-[280px] flex-col gap-9 px-6 py-10 lg:w-[300px]">
        <div className="flex justify-between">
          <button onClick={handleMenuClick}>
            <Icon id="menu" />
          </button>
          <button onClick={handleSearchClick}>
            <Icon id="search" />
          </button>
        </div>
        <div className="flex min-h-0 w-full flex-col gap-2 overflow-y-auto pb-10">
          {interviewList && interviewList.length > 0 ? (
            interviewList.map((item, id) => (
              <MenuItem
                key={id}
                title={item.question}
                onInterviewClick={() => onInterviewClick(item.interviewAnswerId)}
              />
            ))
          ) : (
            <div className="flex justify-center">목록이 없습니다.</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SideMenu;
