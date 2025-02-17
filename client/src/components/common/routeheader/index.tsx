import { ButtonMouseEvent } from '@/types/event';
import Icon from '../icon';

interface RouteHeaderProps {
  prev: string;
  title: string;
  onBackClick: (e: ButtonMouseEvent) => void;
}

const RouteHeader = ({ prev, title, onBackClick }: RouteHeaderProps) => {
  return (
    <div className="flex flex-col gap-1">
      <button className="flex items-center gap-2" onClick={onBackClick} aria-label={`${prev}으로 가기`}>
        <Icon id="back" size={12} className="h-auto md:w-4" />
        <span className="font-semibold text-stone-700 md:text-lg">{prev}</span>
      </button>
      <div className="text-xl font-semibold md:text-2xl">{title}</div>
    </div>
  );
};

export default RouteHeader;
