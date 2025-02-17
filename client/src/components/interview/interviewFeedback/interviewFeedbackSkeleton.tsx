import { Skeleton } from '@/components/ui/skeleton';

const InterviewFeedbackSkeleton = () => {
  return (
    <div className="flex flex-col items-start gap-6 md:p-9">
      <div>
        <Skeleton className="flex w-full" />
      </div>

      <Skeleton className="flex w-full" />
      <Skeleton className="flex w-full" />
      <Skeleton className="flex w-full" />
    </div>
  );
};

export default InterviewFeedbackSkeleton;
