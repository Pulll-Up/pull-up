import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import RouteHeader from '@/components/common/routeheader';
import ReviewList from '@/components/dashboard/reviewList';
import { useGetArchivedProblemAll, useGetArchivedProblemsByTitle } from '@/api/problem';
import { convertSubject } from '@/utils/convertSubject';
import SearchBar from '@/components/common/searchbar';
import { debounce } from 'lodash';
import convertDate from '@/utils/convertDate';
import { InputChangeEvent } from '@/types/event';

const Archive = () => {
  const navigate = useNavigate();
  const [inputValue, setInputValue] = useState('');
  const [debouncedSearchValue, setDebouncedSearchValue] = useState('');

  const { data: archivedProblems } = useGetArchivedProblemAll();
  const bookmarkedProblemDtos =
    archivedProblems?.bookmarkedProblemDtos.map((item) => ({
      id: item.problemId,
      title: item.question,
      date: item.date,
      tags: convertSubject(item.subject),
    })) || [];

  const { data: searchResults } = useGetArchivedProblemsByTitle(debouncedSearchValue);
  const searchList =
    searchResults?.bookmarkedProblemDtos.map((item) => ({
      id: item.problemId,
      title: item.question,
      date: convertDate(item.date),
      tags: convertSubject(item.subject),
    })) || [];

  useEffect(() => {
    const handler = debounce(() => {
      setDebouncedSearchValue(inputValue.trim());
    }, 300);
    handler();
    return () => {
      handler.cancel();
    };
  }, [inputValue]);

  const displayedData = debouncedSearchValue ? searchList : bookmarkedProblemDtos;

  const onHandleBack = () => {
    navigate('/dashboard');
  };

  const onChange = (e: InputChangeEvent) => {
    setInputValue(e.target.value);
  };

  return (
    <section className="flex h-full w-full flex-col gap-3">
      <RouteHeader prev="마이페이지" title="아카이브" onBackClick={onHandleBack} />
      <div className="flex flex-1 flex-col gap-4 rounded-xl bg-white p-4 md:gap-8 md:p-8">
        <SearchBar value={inputValue} onChange={onChange} />
        {displayedData.length > 0 ? (
          <ReviewList data={displayedData} />
        ) : (
          <div className="flex h-full items-center justify-center text-gray-500">아카이빙한 문제가 없습니다.</div>
        )}
      </div>
    </section>
  );
};

export default Archive;
