interface SubjectTagProps {
  title: string;
}

const SubjectTag = ({ title }: SubjectTagProps) => {
  return (
    <div className="rounded border border-secondary-600 bg-secondary-50 px-[4px] py-[2px] text-[10px] text-secondary-600 lg:px-1 lg:py-[2px] lg:text-[11px]">
      {title}
    </div>
  );
};

export default SubjectTag;
