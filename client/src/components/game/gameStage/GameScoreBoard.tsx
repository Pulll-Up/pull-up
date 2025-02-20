import { Player } from '@/types/game';

interface GameScoreBoardProps {
  player: Player;
}

const GameScoreBoard = ({ player }: GameScoreBoardProps) => {
  return (
    <div className="flex flex-col gap-2 rounded-xl bg-white p-4 shadow-sm md:gap-4">
      <div className="flex items-center gap-3">
        <h3 className="text-lg font-bold text-primary-600 md:text-2xl">{player.name}</h3>
      </div>
      <div className="flex min-h-[50px] items-center justify-center px-2 py-1 pb-2 text-4xl font-extrabold text-stone-700 md:h-full md:pb-4 md:text-6xl">
        {player.score}
      </div>
    </div>
  );
};
export default GameScoreBoard;
