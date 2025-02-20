import { useExamAnswer } from '@/hooks/useExamAnswer';
import AnswerOption from '../answeroption';
interface ExamAnswerProps {
  problemId: string;
  questionType: 'SHORT_ANSWER' | 'MULTIPLE_CHOICE';
}

const ExamAnswer = ({ problemId, questionType }: ExamAnswerProps) => {
  const { chosenAnswer, problemOptions, handleTextChange, handleOptionClick, isSolutionPage } =
    useExamAnswer(problemId);

  return (
    <div className="flex w-full flex-col gap-3">
      {questionType === 'MULTIPLE_CHOICE' ? (
        // 객관식 문제
        problemOptions.map((option, index) => (
          <AnswerOption
            key={index}
            id={index + 1}
            content={option.text}
            state={option.state ?? 'default'}
            onClick={() => handleOptionClick(index)}
            disabled={isSolutionPage}
          />
        ))
      ) : (
        // 주관식 문제
        <textarea
          placeholder="정답을 입력하세요."
          className="w-full resize-none rounded-lg border bg-stone-50 px-4 py-2 text-stone-950 placeholder-stone-500 focus:outline-none md:text-lg lg:text-xl"
          value={chosenAnswer}
          disabled={isSolutionPage}
          onChange={handleTextChange}
          maxLength={35}
        />
      )}
    </div>
  );
};

export default ExamAnswer;
