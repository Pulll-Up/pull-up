interface ExamTagProps {
  title: string;
}

const ExamTag = ({ title }: ExamTagProps) => {
  return (
    <div className="rounded border border-primary-400 bg-primary-50 px-[4px] py-[1px] text-[10px] font-semibold text-primary-400 lg:px-1 lg:py-[2px] lg:text-[11px]">
      {title}
    </div>
  );
};

export default ExamTag;
