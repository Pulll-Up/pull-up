import { InputChangeEvent, InputKeyboardEvent } from '@/types/event';
import Icon from '../icon';

interface SearchBarProps {
  value: string;
  onChange: (e: InputChangeEvent) => void;
  onKeyDown?: (e: InputKeyboardEvent) => void;
}

const SearchBar = ({ value, onChange, onKeyDown }: SearchBarProps) => {
  return (
    <div className="flex w-full rounded-lg border border-primary-200 px-4 py-3 shadow-sm">
      <div className="flex w-full items-center gap-2">
        <Icon id="search" size={16} />
        <input
          id="search"
          placeholder="검색어를 입력하세요"
          value={value}
          onChange={onChange}
          onKeyDown={onKeyDown}
          className="w-full text-base outline-none placeholder:text-stone-600"
        />
      </div>
    </div>
  );
};

export default SearchBar;
