import { Bot } from 'lucide-react';

const LoadingPage = () => {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-2">
      <div className="animate-bounce">
        <Bot size={72} className="text-blue-500" strokeWidth={1.5} />
      </div>
      <div className="flex animate-pulse flex-col items-center gap-1 text-2xl font-medium">
        <p>AI가 분석한 결과를 가져오고 있습니다.</p>
        <p>잠시만 기다려주세요!</p>
      </div>
    </div>
  );
};

export default LoadingPage;
