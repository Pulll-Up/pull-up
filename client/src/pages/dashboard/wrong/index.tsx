import { lazy, Suspense, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import RouteHeader from '@/components/common/routeheader';
import { useGetWrongProblemAll, useGetWrongProblemsByTitle } from '@/api/problem';
import convertDate from '@/utils/convertDate';
import { convertSubject } from '@/utils/convertSubject';
import ReviewListSkeleton from '@/components/dashboard/reviewList/reviewListSkeleton';
import SearchBar from '@/components/common/searchbar';
import { debounce } from 'lodash';
import { InputChangeEvent } from '@/types/event';

const ReviewList = lazy(() => import('@/components/dashboard/reviewList'));

const Wrong = () => {
  const navigate = useNavigate();
  const [inputValue, setInputValue] = useState('');
  const [debouncedSearchValue, setDebouncedSearchValue] = useState('');

  // 전체 문제 데이터 가져오기
  const { data: wrongProblems } = useGetWrongProblemAll();
  const wrongProblemDtos =
    wrongProblems?.wrongProblemDtos.map((item) => ({
      id: item.problemId,
      title: item.question,
      date: convertDate(item.date),
      tags: convertSubject(item.subject),
    })) || [];

  // 검색 결과 데이터 가져오기
  const { data: searchResults } = useGetWrongProblemsByTitle(debouncedSearchValue);
  const searchList =
    searchResults?.wrongProblemDtos.map((item) => ({
      id: item.problemId,
      title: item.question,
      date: convertDate(item.date),
      tags: convertSubject(item.subject),
    })) || [];

  // debounce된 검색어 업데이트
  useEffect(() => {
    const handler = debounce(() => {
      setDebouncedSearchValue(inputValue.trim());
    }, 300);
    handler();
    return () => {
      handler.cancel();
    };
  }, [inputValue]);

  // 검색어 유무에 따라 표시할 데이터 결정
  const displayedData = debouncedSearchValue ? searchList : wrongProblemDtos;

  const onHandleBack = () => {
    navigate('/dashboard');
  };

  const onChange = (e: InputChangeEvent) => {
    setInputValue(e.target.value);
  };

  return (
    <section className="flex h-full w-full flex-col gap-3">
      <RouteHeader prev="마이페이지" title="내가 틀린 문제" onBackClick={onHandleBack} />
      <Suspense fallback={<ReviewListSkeleton />}>
        <div className="flex flex-1 flex-col gap-4 rounded-xl bg-white p-4 md:gap-8 md:p-8">
          <SearchBar value={inputValue} onChange={onChange} />
          {displayedData.length > 0 ? (
            <ReviewList data={displayedData} />
          ) : (
            <div className="flex h-full items-center justify-center text-gray-500">틀린 문제가 없습니다.</div>
          )}
        </div>
      </Suspense>
    </section>
  );
};

export default Wrong;
