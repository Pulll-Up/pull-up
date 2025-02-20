const PageSpinner = () => {
  return (
    <div className="flex h-full items-center justify-center bg-Main">
      <div className="relative h-24 py-5">
        <div className="spin-pause absolute flex h-12 w-12 items-center justify-center rounded bg-primary-500 text-4xl font-extrabold text-white">
          P
        </div>
      </div>
    </div>
  );
};

export default PageSpinner;
