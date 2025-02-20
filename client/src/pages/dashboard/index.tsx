import Streak from '@/components/dashboard/charts/Streak';
import Score from '@/components/dashboard/charts/Score';
import Analysis from '@/components/dashboard/charts/Analysis';
import WinningRate from '@/components/dashboard/charts/WinningRate';
import useResponsive from '@/hooks/useResponsive';
import MobileDashboardContainer from './MobileDashboardContainer';
import ChartContainer from './ChartContainer';
import DashboardFeedback from '@/components/dashboard/dashboardFeedback';

const DashBoardPage = () => {
  const { isMobile, isTabletMd } = useResponsive();

  return (
    <div className="w-full">
      {isMobile || isTabletMd ? (
        <MobileDashboardContainer />
      ) : (
        <div className="grid h-full w-full grid-cols-[2fr_1fr] gap-4 bg-Main">
          <div className="grid grid-rows-[1.6fr_1fr] gap-4 rounded-2xl">
            <div className="flex flex-col rounded-2xl bg-white p-5">
              <ChartContainer icon="feedback" title="오늘의 문제 및 피드백">
                <DashboardFeedback />
              </ChartContainer>
            </div>
            <div className="grid grid-cols-[1fr_1fr] gap-4">
              <div className="flex flex-col rounded-2xl bg-white p-5">
                <ChartContainer icon="time" title="오늘의 문제 풀이 현황">
                  <Streak />
                </ChartContainer>
              </div>
              <div className="flex flex-col rounded-2xl bg-white p-5">
                <ChartContainer icon="crown" title="게임 승률">
                  <WinningRate />
                </ChartContainer>
              </div>
            </div>
          </div>
          <div className="grid grid-rows-[1.6fr_1fr] gap-4">
            <div className="flex flex-col rounded-2xl bg-white p-5">
              <ChartContainer icon="smile" title="과목별 강점 분석">
                <Analysis />
              </ChartContainer>
            </div>
            <div className="flex flex-col rounded-2xl bg-white p-5">
              <ChartContainer icon="score" title="시험 점수 현황">
                <Score />
              </ChartContainer>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DashBoardPage;
