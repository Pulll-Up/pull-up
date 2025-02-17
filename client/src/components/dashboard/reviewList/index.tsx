import ReviewCard from '@/components/dashboard/reviewCard';
import convertDate from '@/utils/convertDate';

interface ReviewListProps {
  data: {
    id: number | string;
    title: string;
    date: string;
    tags: string[] | string;
  }[];
  isProblem?: boolean;
}

const ReviewList = ({ data, isProblem = true }: ReviewListProps) => {
  return (
    <div className="flex flex-col gap-4">
      {data.map((data) => (
        <ReviewCard
          isProblem={isProblem}
          key={data.id}
          id={data.id}
          title={data.title}
          subtitle={convertDate(data.date)}
          tags={data.tags}
        />
      ))}
    </div>
  );
};

export default ReviewList;
