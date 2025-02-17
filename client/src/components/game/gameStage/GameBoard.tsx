import { useEffect, useState } from 'react';
import GameCard from './GameCard';
import { Card, PlayerType } from '@/types/game';
import { useWebSocketStore } from '@/stores/useWebSocketStore';

interface GameBoardProps {
  playerType: PlayerType;
  problems: Card[];
}

const GameBoard = ({ playerType, problems }: GameBoardProps) => {
  const { sendMessage, roomInfo, isCheckedAnswer, completeCheckAnswer } = useWebSocketStore();

  const [selectedCards, setSelectedCards] = useState<number[]>([]);
  const [shake, setShake] = useState(false);

  useEffect(() => {
    if (isCheckedAnswer) {
      setShake(true);
    }
  }, [isCheckedAnswer]);

  const handleClickCard = (index: number) => {
    if (selectedCards.length === 1 && problems[selectedCards[0]].disabled) {
      setSelectedCards([index]);
      return;
    }

    if (selectedCards.includes(index)) {
      setSelectedCards(selectedCards.filter((i) => i !== index));
      return;
    }

    if (selectedCards.length < 2) {
      setSelectedCards([...selectedCards, index]);
    }

    if (selectedCards.length === 1) {
      checkCardPair(selectedCards[0], index);
    }
  };

  const checkCardPair = (cardIndex1: number, cardIndex2: number) => {
    sendMessage('/app/card/check', {
      checkType: 'SUBMIT',
      roomId: roomInfo.roomId,
      playerType,
      contents: [problems[cardIndex1].content, problems[cardIndex2].content],
    });

    setTimeout(() => {
      setShake(false);

      completeCheckAnswer();
      setSelectedCards([]);
    }, 500);
  };

  return (
    <div className="grid grid-cols-4 grid-rows-4 rounded-md bg-primary-400 p-1 shadow-sm">
      {problems.map((card, i) => (
        <GameCard
          key={i}
          shake={shake}
          card={card}
          isSelected={selectedCards.includes(i)}
          onClick={() => handleClickCard(i)}
        />
      ))}
    </div>
  );
};
export default GameBoard;
