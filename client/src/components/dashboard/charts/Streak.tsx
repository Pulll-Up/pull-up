import { useGetStreak } from '@/api/interview';
import { cn } from '@/lib/utils';
import { useEffect, useState } from 'react';
import ActivityCalendar, { DayIndex } from 'react-activity-calendar';

const Streak = () => {
  const { data: streakData, isLoading, isError } = useGetStreak();

  const tomorrow = new Date();
  tomorrow.setDate(new Date().getDate() + 1);

  const weekStart = tomorrow.getDay() as DayIndex;

  // 스트릭 애니메이션
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    setTimeout(() => setVisible(true), 100);
  }, []);

  if (isLoading) return <>불러오는 중...</>;
  if (isError || !streakData) return <>스트릭 불러오기에 실패했습니다.</>;

  return (
    <div
      className={cn(
        {
          'translate-y-0 opacity-100': visible,
          'translate-y-[10px] opacity-0': !visible,
        },
        'transition-all duration-1000 ease-out',
      )}
    >
      <ActivityCalendar
        data={streakData.dailySolvedHistories || []}
        weekStart={weekStart}
        blockSize={17}
        blockMargin={4}
        hideColorLegend={true}
        hideMonthLabels={true}
        hideTotalCount={true}
        colorScheme="light"
        maxLevel={1}
        theme={{
          light: ['#eeeff1', '#6356f8'],
        }}
      />
    </div>
  );
};

export default Streak;
