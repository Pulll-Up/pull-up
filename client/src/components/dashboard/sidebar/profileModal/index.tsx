import { useUpdateInterestSubjects } from '@/api/member';
import SubjectSelector from '@/components/common/csConditionSelector/subjectSelector';
import { SUBJECT_OPTIONS } from '@/components/common/csConditionSelector/subjectSelector/SubjectOptions';
import { Button } from '@/components/ui/button';
import { Subject } from '@/types/member';
import { DialogClose } from '@radix-ui/react-dialog';
import { useState } from 'react';
import { toast } from 'react-toastify';

const ProfileModal = () => {
  const [selectedSubjects, setSelectedSubjects] = useState<Subject[]>([]);
  const isDisabled = selectedSubjects.length === 0;

  const handleSubjectClick = (id: Subject) => {
    setSelectedSubjects((prev) => (prev.includes(id) ? prev.filter((subjectId) => subjectId !== id) : [...prev, id]));
  };

  const updateInterestSubjectsMutation = useUpdateInterestSubjects();

  const onSubmit = () => {
    updateInterestSubjectsMutation(selectedSubjects, {
      onSuccess: () => {
        toast.success('관심 과목이 수정되었습니다.', { position: 'bottom-center' });
      },
    });
  };

  return (
    <div className="flex w-full flex-col gap-8 px-10 py-4">
      <div className="flex flex-col justify-between gap-3">
        {SUBJECT_OPTIONS.map((subject) => (
          <SubjectSelector
            key={subject.id}
            id={subject.id}
            name={subject.name}
            icon={subject.icon}
            isSelected={selectedSubjects.includes(subject.id)}
            onClick={handleSubjectClick}
          />
        ))}
      </div>
      <DialogClose asChild>
        <Button disabled={isDisabled} onClick={() => onSubmit()}>
          수정 완료
        </Button>
      </DialogClose>
    </div>
  );
};

export default ProfileModal;
