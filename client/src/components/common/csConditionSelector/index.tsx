import { useState } from 'react';
import SubmitButton from '../submitButton';
import { SUBJECT_OPTIONS } from './subjectSelector/SubjectOptions';
import { LEVELS_OPTIONS } from './levelSelector/levelOptions';
import SubjectSelector from './subjectSelector';
import LevelSelector from './levelSelector';
import { Level } from '@/types/exam';
import { Subject } from '@/types/member';

interface CsConditionSelectorProps {
  isExam?: boolean;
  title: string;
  text: string;
  onClick: (level: Level | null, subjects: Subject[]) => void;
}

const CsConditionSelector = ({ isExam = false, title, text, onClick }: CsConditionSelectorProps) => {
  const [selectedLevel, setSelectedLevel] = useState<Level | null>(null);
  const [selectedSubjects, setSelectedSubjects] = useState<Subject[]>([]);

  const handleLevelClick = (id: Level) => setSelectedLevel(id);

  const handleSubjectClick = (id: Subject) => {
    setSelectedSubjects((prev) => (prev.includes(id) ? prev.filter((subjectId) => subjectId !== id) : [...prev, id]));
  };

  const handleSubmit = () => {
    onClick(selectedLevel, selectedSubjects);
  };

  const isDisabled = selectedSubjects.length === 0 || (isExam && !selectedLevel);

  return (
    <div className="flex h-auto w-[300px] flex-col gap-2 rounded-2xl bg-white p-5 shadow-sm md:min-w-[400px] md:p-8 xl:min-w-[450px]">
      <div className="flex flex-col gap-4">
        <div className="flex flex-col gap-2">
          <div className="font-semibold text-stone-700 md:text-lg">{title}</div>
          <div className="flex flex-col justify-center gap-2">
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
        </div>
        {isExam && (
          <div className="flex flex-col gap-2 pt-4">
            <div className="font-semibold text-stone-700 md:text-lg">난이도 선택</div>
            <div className="flex justify-center gap-2">
              {LEVELS_OPTIONS.map((level) => (
                <LevelSelector
                  key={level.id}
                  id={level.id}
                  name={level.name}
                  isSelected={selectedLevel === level.id}
                  onClick={handleLevelClick}
                />
              ))}
            </div>
          </div>
        )}
        <SubmitButton
          text={text}
          onClick={handleSubmit}
          disabled={isDisabled}
          color={isDisabled ? 'gray' : 'primary'}
        />
      </div>
    </div>
  );
};

export default CsConditionSelector;
