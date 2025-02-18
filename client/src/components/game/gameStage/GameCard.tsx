import { cn } from '@/lib/utils';
import { Card } from '@/types/game';

interface GameCardProps {
  card: Card;
  isSelected: boolean;
  onClick: () => void;
  isShake: boolean;
}

const GameCard = ({ card, isSelected, isShake, onClick }: GameCardProps) => {
  return (
    <>
      {card.disabled ? (
        <div className="m-1 rounded-md bg-primary-600 p-2"></div>
      ) : (
        <div
          className={cn(
            {
              [`border-[#65efb6]`]: isSelected,
              'border-white': !isSelected,
              'animate-shake border-[#ff716c]': isShake && isSelected,
            },
            'm-1 flex cursor-pointer select-none items-center justify-center rounded-md border-4 bg-white p-2 text-sm sm:text-base',
          )}
          onClick={onClick}
        >
          {card.content}
        </div>
      )}
    </>
  );
};
export default GameCard;
