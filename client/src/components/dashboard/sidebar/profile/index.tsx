import SubjectTag from '@/components/common/subjectTag';
import useResponsive from '@/hooks/useResponsive';
import Modal from '@/components/common/modal';
import ProfileModal from '../profileModal';

interface ProfileProps {
  image: string;
  name: string;
  email: string;
  subjects: string[];
}

const Profile = ({ image, name, email, subjects }: ProfileProps) => {
  const { isDesktop } = useResponsive();

  return (
    <div className="flex flex-row-reverse items-start lg:block">
      <div className="mt-[-2px] flex justify-end lg:mt-0 lg:w-full">
        <Modal buttonSize="xs" triggerName="과목 수정" triggerColor="transparent" isOutsideClickable={true}>
          <ProfileModal />
        </Modal>
      </div>
      <div className="flex w-[150px] flex-col justify-evenly gap-3 lg:w-auto lg:translate-y-[-10px] lg:justify-start lg:gap-1">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center">
          {isDesktop && (
            <div className="h-16 w-16 overflow-hidden rounded-full">
              <img src={image} className="h-full w-full object-cover" />
            </div>
          )}
          <div className="flex flex-col">
            <span className="text-xl font-bold">{name}</span>
            <span className="text-sm font-semibold text-stone-700">{email}</span>
          </div>
        </div>
        <div className="flex flex-col gap-2 lg:flex-row lg:items-center">
          <span className="w-16 text-sm font-semibold text-stone-700 lg:text-center">관심 과목</span>
          <div className="flex flex-1 flex-wrap gap-1">
            {subjects.map((subject, id) => (
              <SubjectTag key={id} title={subject} />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
