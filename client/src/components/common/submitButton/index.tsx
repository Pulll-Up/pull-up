import { cn } from '@/lib/utils';
import { ButtonMouseEvent } from '@/types/event';

interface SubmitButtonProps {
  onClick: (e: ButtonMouseEvent) => void;
  text: string;
  color?: 'primary' | 'secondary' | 'gray';
  disabled?: boolean;
}

const SubmitButton = ({ onClick, text, color = 'primary', disabled = false }: SubmitButtonProps) => {
  const COLOR_PROPS = {
    primary: 'bg-primary-500 text-white',
    secondary: 'bg-secondary-500 text-white',
    gray: 'bg-gray-200 text-gray-500',
  };

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={cn(COLOR_PROPS[color], 'w-full rounded-xl py-4 text-lg font-semibold xl:py-5 xl:text-xl')}
    >
      {text}
    </button>
  );
};

export default SubmitButton;
