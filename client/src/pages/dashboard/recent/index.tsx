import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import RouteHeader from '@/components/common/routeheader';
import ReviewList from '@/components/dashboard/reviewList';
import { useGetExamAll } from '@/api/exam';
import convertDate from '@/utils/convertDate';
import { convertSubject } from '@/utils/convertSubject';

const Recent = () => {
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState('');
  const { data: examAll } = useGetExamAll();

  const examData =
    examAll?.getExamResponses.map((exam) => ({
      id: exam.examId,
      title: exam.examName,
      date: convertDate(exam.date),
      tags: convertSubject(exam.subjects),
    })) || [];

  const onHandleBack = () => {
    navigate('/dashboard');
  };

  return (
    <section className="flex w-full flex-col gap-3">
      <RouteHeader prev="마이페이지" title="최근에 푼 모의고사" onBackClick={onHandleBack} />
      <div className="flex flex-1 flex-col rounded-xl bg-white px-4 py-6 md:p-8">
        <ReviewList data={examData} isProblem={false} />
      </div>
    </section>
  );
};

export default Recent;
