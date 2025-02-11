import Icon from '@/components/common/icon';
import ExamTag from '@/components/common/examTag';
import { Link } from 'react-router';

interface ReviewCardProps {
  id: number;
  title: string;
  subtitle: string;
  tags: string[] | string;
  isProblem: boolean;
}

const ReviewCard = ({ id, title, subtitle, tags, isProblem }: ReviewCardProps) => {
  const renderTags = () => {
    if (typeof tags === 'string') {
      return <ExamTag title={tags} />;
    }

    return tags.map((tag, id) => <ExamTag key={id} title={tag} />);
  };

  return (
    <Link
      to={isProblem ? `/exam/problem/${id}` : `/exam/${id}/result`}
      className="flex flex-col gap-3 rounded-lg px-3 py-2 shadow-sm"
    >
      <div className="flex w-full items-start justify-between">
        <div className="flex flex-col items-start">
          <span className="break-keep font-semibold text-stone-800">{title}</span>
          <span className="text-sm font-semibold text-stone-400">{subtitle}</span>
        </div>
        <Icon id="list" size={32} />
      </div>
      <div className="flex flex-wrap gap-2">{renderTags()}</div>
    </Link>
  );
};

export default ReviewCard;
