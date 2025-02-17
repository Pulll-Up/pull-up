import { Bot } from 'lucide-react';

const LoadingLayout = () => {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-2">
      <div className="animate-bounce">
        <Bot size={48} className="text-blue-500" strokeWidth={1.5} />
      </div>
      <div className="flex animate-pulse flex-col items-center gap-1 text-lg font-medium">
        <p>AI가 답변을 분석중입니다.</p>
        <p>잠시만 기다려주세요!</p>
      </div>
    </div>
  );
};

export default LoadingLayout;
