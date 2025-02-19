import { useEffect, useState } from 'react';
import ExamTag from '@/components/common/examTag';
import Icon from '@/components/common/icon';
import useResponsive from '@/hooks/useResponsive';
import { Link } from 'react-router-dom';

interface CardProps {
  link: string;
  title: string;
  dataList: {
    id: number | string;
    content: string;
    date?: string;
    subjects: string[];
  }[];
}

const Card = ({ link, title, dataList }: CardProps) => {
  const { isMobile, isTabletMd } = useResponsive();
  const [index, setIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setIndex((prevIndex) => (prevIndex + 1) % dataList.length);
    }, 4400);

    return () => clearInterval(interval);
  }, [dataList.length]);

  const currentData = dataList[index];

  const generatedLink =
    currentData.id === 0
      ? link
      : link === 'recent'
        ? `/exam/${currentData.id}/result`
        : `/exam/problem/${currentData.id}`;

  return (
    <div className="flex w-full flex-col gap-2 pb-2">
      <nav>
        <Link to={link} className="flex items-center justify-between pl-2">
          <span className="text-sm font-bold text-stone-900">{title}</span>
          {isMobile || isTabletMd ? (
            <Icon id="list" size={20} aria-label={`${title}로 이동`} />
          ) : (
            <Icon id="list" size={25} aria-label={`${title}로 이동`} />
          )}
        </Link>
      </nav>

      <Link to={generatedLink} className="flex h-full w-full flex-col items-center md:h-[140px] lg:h-full">
        <div className="flex h-full w-full rounded-2xl bg-white p-3 shadow-sm md:p-4">
          <div key={index} className="animate-slide-fade-up flex w-full flex-col gap-2">
            <div className="relative h-[50px] w-full lg:h-auto">
              <div className="flex h-full flex-col justify-center">
                <span className="line-clamp-2 overflow-hidden text-ellipsis text-left text-base font-medium text-stone-800 md:text-xs lg:mb-2 lg:line-clamp-1 lg:text-sm">
                  {currentData.content}
                </span>
                <span className="text-xs text-stone-400 lg:text-base">{currentData.date}</span>
              </div>
            </div>

            <div className="flex flex-wrap gap-2">
              {currentData.subjects && currentData.subjects.map((subject, id) => <ExamTag key={id} title={subject} />)}
            </div>
          </div>
        </div>
      </Link>
    </div>
  );
};

export default Card;
