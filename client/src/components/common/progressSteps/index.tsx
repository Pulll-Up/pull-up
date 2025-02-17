import React, { Fragment } from 'react';
import { Check } from 'lucide-react';

interface ProgressStepsProps {
  currentStep: number;
  totalStep: number;
}

const ProgressSteps = ({ currentStep, totalStep }: ProgressStepsProps) => {
  return (
    <>
      <style>
        {`
          @keyframes numberToCheck {
            0% { transform: rotate(-180deg) scale(0); opacity: 0; }
            100% { transform: rotate(0) scale(1); opacity: 1; }
          }
          
          @keyframes lineExpand {
            from { transform: scaleX(0); }
            to { transform: scaleX(1); }
          }
          
          .animate-number-to-check {
            animation: numberToCheck 0.5s ease-out forwards;
          }
          
          .animate-line-expand {
            animation: lineExpand 0.5s ease-out forwards;
            transform-origin: left;
          }
        `}
      </style>
      <div className="flex w-full max-w-lg items-center justify-center gap-2">
        {Array.from({ length: totalStep }).map((_, index) => (
          <Fragment key={index}>
            <div className="relative">
              <div
                className={`flex h-10 w-10 items-center justify-center rounded-full transition-all duration-500 ${
                  currentStep > index + 1
                    ? 'border-2 border-primary-500' // 완료된 단계
                    : currentStep === index + 1
                      ? 'bg-primary-500' // 현재 단계
                      : 'border-2 border-stone-300 bg-white' // 미래 단계
                }`}
              >
                {currentStep > index + 1 ? (
                  <Check className="animate-number-to-check h-5 w-5 text-primary-500" />
                ) : (
                  <span
                    className={`text-lg font-semibold transition-colors duration-500 ${
                      currentStep >= index + 1 ? 'text-white' : 'text-stone-700'
                    }`}
                  >
                    {index + 1}
                  </span>
                )}
              </div>
            </div>

            {/* Connecting line */}
            {index < totalStep - 1 && (
              <div
                className={`h-1 w-20 rounded-lg transition-colors duration-500 ${
                  currentStep > index + 1 ? 'animate-line-expand bg-primary-500' : 'bg-stone-300'
                }`}
              />
            )}
          </Fragment>
        ))}
      </div>
    </>
  );
};

export default ProgressSteps;
