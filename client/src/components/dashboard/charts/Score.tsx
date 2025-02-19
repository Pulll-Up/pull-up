import { Bar, BarChart, CartesianGrid, XAxis } from 'recharts';

import { ChartConfig, ChartContainer, ChartTooltip, ChartTooltipContent } from '@/components/ui/chart';
import { useGetScore } from '@/api/exam';

const chartConfig = {
  score: {
    label: '점수',
    color: '#60a5fa',
  },
} satisfies ChartConfig;

const DEFAULT_SCORES = [
  { round: '', score: 1 },
  { round: '', score: 1 },
  { round: '', score: 1 },
  { round: '', score: 1 },
  { round: '', score: 1 },
];

const Score = () => {
  const { data: scoreData, isLoading, isError } = useGetScore();

  if (isLoading) return <>불러오는 중...</>;
  if (isError || !scoreData) return <>차트 불러오기에 실패했어요</>;

  const mergedData = DEFAULT_SCORES.map((item, index) => ({
    ...item,
    ...(scoreData.examScoreDtos[4 - index] || {}),
  }));

  return (
    <ChartContainer config={chartConfig} className="w-[80%] overflow-hidden">
      <BarChart accessibilityLayer data={mergedData}>
        <CartesianGrid vertical={false} />
        <XAxis
          tickFormatter={(value) => (value ? `${value}회` : '')}
          dataKey="round"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
        />
        <ChartTooltip content={<ChartTooltipContent />} />
        <Bar dataKey="score" fill={chartConfig.score.color} radius={4} />
      </BarChart>
    </ChartContainer>
  );
};

export default Score;
