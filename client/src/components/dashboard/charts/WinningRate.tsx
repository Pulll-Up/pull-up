import { useGetWinningRate } from '@/api/game';
import { ChartConfig, ChartContainer, ChartTooltip, ChartTooltipContent } from '@/components/ui/chart';
import { Label, PolarRadiusAxis, RadialBar, RadialBarChart } from 'recharts';

const chartConfig = {
  winCount: {
    label: '승리',
    color: '#6356f8',
  },
  drawCount: {
    label: '무승부',
    color: '#d3d9e6',
  },
  loseCount: {
    label: '패배',
    color: '#eeeff1',
  },
} satisfies ChartConfig;

// const mergedData = { winCount: 0, loseCount: 0, drawCount: 1 };

const WinningRate = () => {
  const { data: winningRateData, isLoading, isError } = useGetWinningRate();

  if (isLoading) return <>불러오는 중...</>;
  if (isError || !winningRateData) return <>차트 불러오기에 실패했어요</>;
  if (!winningRateData.winCount && !winningRateData.loseCount && !winningRateData.drawCount)
    return <>게임을 하고 승률을 확인하세요!</>;

  return (
    <ChartContainer config={chartConfig} className="mx-auto h-[190px] w-full max-w-[250px] overflow-hidden">
      <RadialBarChart
        className="translate-y-10"
        data={[winningRateData]}
        endAngle={180}
        innerRadius={80}
        outerRadius={130}
      >
        <ChartTooltip cursor={false} content={<ChartTooltipContent hideLabel />} />
        <PolarRadiusAxis tick={false} tickLine={false} axisLine={false}>
          <Label
            content={({ viewBox }) => {
              if (viewBox && 'cx' in viewBox && 'cy' in viewBox) {
                return (
                  <text x={viewBox.cx} y={viewBox.cy} textAnchor="middle">
                    <tspan x={viewBox.cx} y={(viewBox.cy || 0) - 16} className="fill-foreground text-2xl font-bold">
                      {Math.round(
                        (winningRateData.winCount /
                          (winningRateData.loseCount + winningRateData.winCount + winningRateData.drawCount)) *
                          100,
                      )}
                      %
                    </tspan>
                  </text>
                );
              }
            }}
          />
        </PolarRadiusAxis>
        <RadialBar
          dataKey="loseCount"
          fill={chartConfig.loseCount.color}
          stackId="a"
          cornerRadius={5}
          className="stroke-transparent stroke-2"
        />
        <RadialBar
          dataKey="drawCount"
          stackId="a"
          cornerRadius={5}
          fill={chartConfig.drawCount.color}
          className="stroke-transparent stroke-2"
        />
        <RadialBar
          dataKey="winCount"
          stackId="a"
          cornerRadius={5}
          fill={chartConfig.winCount.color}
          className="stroke-transparent stroke-2"
        />
      </RadialBarChart>
    </ChartContainer>
  );
};

export default WinningRate;
