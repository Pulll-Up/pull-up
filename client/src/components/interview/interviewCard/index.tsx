import React from 'react';
import Keyword from '../keywordList/keyword';
import { ButtonMouseEvent } from '@/types/event';

interface InterviewCardProps {
  title: string;
  keywords: string[];
  onHintClick: (e: ButtonMouseEvent) => void;
  hint: boolean;
}

const InterviewCard = ({ title, keywords, onHintClick, hint }: InterviewCardProps) => {
  return (
    <div className="relative h-[200px] w-full md:h-[340px] lg:h-[372px]" style={{ perspective: '2000px' }}>
      <div
        className="relative h-full w-full transition-all duration-700 ease-in-out"
        style={{
          transformStyle: 'preserve-3d',
          transform: hint ? 'rotateY(-180deg)' : 'rotateY(0deg)',
        }}
      >
        {/* 앞면 - 문제 */}
        <div className="absolute h-full w-full" style={{ backfaceVisibility: 'hidden' }}>
          <div className="flex h-full w-full flex-col items-center justify-start rounded-xl bg-white px-6 py-4 shadow-sm">
            <div className="flex w-full justify-end">
              <button
                className="rounded-lg bg-stone-950 px-4 py-2 text-sm font-extrabold text-white transition-colors duration-200"
                onClick={onHintClick}
              >
                힌트 보기
              </button>
            </div>
            <div className="flex flex-1 items-center">
              <span className="break-all text-xl font-extrabold md:text-2xl lg:text-3xl">{title}</span>
            </div>
            <div className="h-9"></div>
          </div>
        </div>

        {/* 뒷면 - 힌트 */}
        <div
          className="absolute h-full w-full"
          style={{
            backfaceVisibility: 'hidden',
            transform: 'rotateY(180deg)',
          }}
        >
          <div className="flex h-full w-full flex-col items-center justify-start rounded-xl bg-white px-6 py-4 shadow-sm">
            <div className="flex w-full justify-end">
              <button
                className="rounded-lg bg-stone-950 px-2 py-1 text-xs font-extrabold text-white transition-colors duration-200 md:px-4 md:py-2 md:text-sm"
                onClick={onHintClick}
              >
                문제 보기
              </button>
            </div>
            <div className="flex flex-1 items-center">
              <div className="flex flex-col items-center gap-8">
                <span className="text-2xl font-bold text-primary-500 lg:text-3xl">키워드</span>
                <div className="flex flex-wrap justify-center gap-3">
                  {keywords.map((keyword, index) => (
                    <Keyword key={index} title={keyword} color="gray" />
                  ))}
                </div>
              </div>
            </div>
            <div className="h-9"></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InterviewCard;
