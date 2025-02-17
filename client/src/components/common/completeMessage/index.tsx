import React from 'react';

interface CompleteMessageProps {
  title: string;
  subTitle: string;
}

const CompleteMessage = ({ title, subTitle }: CompleteMessageProps) => {
  return (
    <>
      <style>
        {`
          @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
          }

          @keyframes slideUp {
            from { transform: translateY(20px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
          }

          @keyframes expand {
            from { width: 0; }
            to { width: 100%; }
          }

          .animate-fade-in {
            animation: fadeIn 0.5s ease-out forwards;
          }

          .animate-slide-up {
            animation: slideUp 0.5s ease-out 0.2s forwards;
            opacity: 0;
          }

          .animate-expand {
            animation: expand 0.5s ease-out 0.5s forwards;
          }
        `}
      </style>
      <div className="animate-fade-in flex flex-col items-center justify-center gap-6 opacity-0">
        <div className="relative">
          <span className="animate-slide-up text-3xl font-bold text-primary-500">{title}</span>
          <div className="animate-expand absolute -bottom-2 left-0 h-1 w-0 bg-primary-500" />
        </div>
        <span className="text text-stone-700">{subTitle}</span>
      </div>
    </>
  );
};

export default CompleteMessage;
