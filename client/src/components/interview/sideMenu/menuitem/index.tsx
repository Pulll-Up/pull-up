interface MenuItemProps {
  title: string;
  onInterviewClick: () => void;
  onMouseEnter?: () => void; // prefetch를 위한 props
}

const MenuItem = ({ title, onInterviewClick, onMouseEnter }: MenuItemProps) => {
  return (
    <div className="flex w-full flex-col gap-2">
      <button
        onClick={onInterviewClick}
        onMouseEnter={onMouseEnter}
        className="overflow-hidden truncate whitespace-nowrap px-1 text-left text-lg text-gray-700"
      >
        {title}
      </button>
      <hr className="border border-gray-200" />
    </div>
  );
};

export default MenuItem;
