import { PolarAngleAxis, PolarGrid, Radar, RadarChart } from 'recharts';

import { ChartConfig, ChartContainer, ChartTooltip, ChartTooltipContent } from '@/components/ui/chart';
import { useGetCorrectRate } from '@/api/exam';
import { convertSubject } from '@/utils/convertSubject';

const chartConfig = {
  rate: {
    label: '점수',
    color: '#6356f8',
  },
} satisfies ChartConfig;

const Analysis = () => {
  const { data: analysisData, isPending, isError } = useGetCorrectRate();

  if (isPending) return <>불러오는 중...</>;
  if (isError) return <>차트 불러오기에 실패했어요</>;

  return (
    <div className="flex aspect-[16/13] w-full justify-center overflow-hidden p-2">
      <ChartContainer config={chartConfig} className="h-full">
        <RadarChart data={analysisData.examStrengthDtos}>
          <ChartTooltip
            formatter={(value, name) => (name === 'correctRate' ? [value] : [name, value])}
            cursor={false}
            content={<ChartTooltipContent />}
            labelFormatter={convertSubject}
          />
          <PolarAngleAxis dataKey="subject" tickFormatter={convertSubject} />
          <PolarGrid />
          <Radar dataKey="correctRate" fill={chartConfig.rate.color} fillOpacity={0.4} />
        </RadarChart>
      </ChartContainer>
    </div>
  );
};

export default Analysis;
