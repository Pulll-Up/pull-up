import ExamTag from '@/components/common/examTag';
import Icon from '@/components/common/icon';
import useResponsive from '@/hooks/useResponsive';
import { Link } from 'react-router-dom';

interface CardProps {
  link: string;
  title: string;
  data: {
    id: number;
    content: string;
    date?: string;
    subjects: string[];
  };
}

const Card = ({ link, title, data }: CardProps) => {
  const { isMobile, isTabletMd } = useResponsive();
  const generatedLink =
    data.id === 0 ? link : link === 'recent' ? `/exam/${data.id}/result` : `/exam/problem/${data.id}`;

  return (
    <div className="flex w-full flex-col gap-2 pb-2">
      <nav>
        <div className="flex items-center justify-between">
          <span className="text-md ml-2 font-bold text-stone-900 lg:text-lg">{title}</span>
          <Link to={link}>
            {isMobile || isTabletMd ? (
              <Icon id="list" size={20} aria-label={`${title}로 이동`} />
            ) : (
              <Icon id="list" size={30} aria-label={`${title}로 이동`} />
            )}
          </Link>
        </div>
      </nav>

      <Link to={generatedLink} className="flex w-full flex-col items-center">
        <button className="flex w-[99%] flex-col gap-6 rounded-2xl bg-white p-5 shadow-sm md:px-3 md:py-4">
          <div className="flex h-[30px] w-full justify-between md:h-[45px] lg:h-auto">
            <span className="text:base text-left font-medium text-stone-800 md:text-xs lg:text-base">
              {data.content}
            </span>
            <span className="text-xs text-stone-400 lg:text-base">{data.date}</span>
          </div>
          <div className="flex flex-wrap gap-2">
            {data.subjects.map((subject, id) => (
              <ExamTag key={id} title={subject} />
            ))}
          </div>
        </button>
      </Link>
    </div>
  );
};

export default Card;
