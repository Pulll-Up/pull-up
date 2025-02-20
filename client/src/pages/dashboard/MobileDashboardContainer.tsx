import Analysis from '@/components/dashboard/charts/Analysis';
import Score from '@/components/dashboard/charts/Score';
import Streak from '@/components/dashboard/charts/Streak';
import WinningRate from '@/components/dashboard/charts/WinningRate';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import ChartContainer from './ChartContainer';
import DashboardFeedback from '@/components/dashboard/dashboardFeedback';

const ACTIVE_STYLE =
  'data-[state=active]:border-b-[3px] data-[state=active]:border-primary-500 data-[state=active]:text-primary-500';

const MobileDashboardContainer = () => {
  return (
    <Tabs defaultValue="today">
      <TabsList className="grid w-full grid-cols-5">
        <TabsTrigger className={ACTIVE_STYLE} value="today">
          오늘의 문제
        </TabsTrigger>
        <TabsTrigger className={ACTIVE_STYLE} value="streak">
          풀이 현황
        </TabsTrigger>
        <TabsTrigger className={ACTIVE_STYLE} value="score">
          시험 점수
        </TabsTrigger>
        <TabsTrigger className={ACTIVE_STYLE} value="analysis">
          과목별 강점
        </TabsTrigger>
        <TabsTrigger className={ACTIVE_STYLE} value="winningRate">
          게임 승률
        </TabsTrigger>
      </TabsList>
      <TabsContent value="today" className="w-full">
        <div className="flex min-h-[300px] flex-col rounded-2xl bg-white p-5">
          <ChartContainer>
            <div className="flex w-full items-center justify-center">
              <DashboardFeedback />
            </div>
          </ChartContainer>
        </div>
      </TabsContent>
      <TabsContent value="streak">
        <div className="flex max-h-[400px] min-h-[300px] flex-col rounded-2xl bg-white p-5">
          <p className="text-md font-bold text-stone-800">49일 동안의 오늘의 문제 스트릭</p>
          <ChartContainer>
            <Streak />
          </ChartContainer>
        </div>
      </TabsContent>
      <TabsContent value="score">
        <div className="flex max-h-[400px] min-h-[300px] flex-col rounded-2xl bg-white p-5">
          <p className="text-md font-bold text-stone-800">최근 5회 시험 점수 확인하기</p>
          <ChartContainer>
            <Score />
          </ChartContainer>
        </div>
      </TabsContent>
      <TabsContent value="analysis">
        <div className="flex max-h-[500px] min-h-[300px] flex-col rounded-2xl bg-white p-5">
          <p className="text-md font-bold text-stone-800">CS 과목별 강점 확인하기</p>
          <ChartContainer>
            <Analysis />
          </ChartContainer>
        </div>
      </TabsContent>
      <TabsContent value="winningRate">
        <div className="flex max-h-[400px] min-h-[300px] flex-col rounded-2xl bg-white p-5">
          <p className="text-md font-bold text-stone-800">카드 게임 승률 보기</p>
          <ChartContainer>
            <WinningRate />
          </ChartContainer>
        </div>
      </TabsContent>
    </Tabs>
  );
};

export default MobileDashboardContainer;
