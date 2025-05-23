import { cn } from '@/lib/utils';
import { Level } from '@/types/exam';

interface LevelSelectorProps {
  id: Level;
  name: string;
  isSelected: boolean;
  onClick: (id: Level) => void;
}

const LevelSelector = ({ id, name, isSelected, onClick }: LevelSelectorProps) => {
  return (
    <button
      onClick={() => onClick(id)}
      className={cn(
        'flex flex-1 justify-center rounded-xl border py-1 text-sm font-semibold md:text-base',
        isSelected
          ? 'border-primary-500 bg-primary-500 text-white'
          : 'border-2 border-primary-500 text-primary-500 hover:bg-primary-50',
      )}
    >
      {name}
    </button>
  );
};

export default LevelSelector;
