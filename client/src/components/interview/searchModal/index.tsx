import Icon from '@/components/common/icon';
import SearchBar from '@/components/common/searchbar';
import { useState } from 'react';
import MenuItem from '../sideMenu/menuitem';
import { InterviewListResponse } from '@/types/response/interview';
import { getInterviewListByKeyword } from '@/api/interview';
import { debounce } from 'lodash';
import { InputChangeEvent } from '@/types/event';

interface SearchModalProps {
  onClose: () => void;
  onInterviewClick: (interviewId: string) => void;
}

const SearchModal = ({ onClose, onInterviewClick }: SearchModalProps) => {
  const [searchList, setSearchList] = useState<InterviewListResponse[]>();
  const [value, setValue] = useState('');

  const handleSearch = debounce(async (searchValue: string) => {
    if (searchValue.trim().length > 0) {
      const data = await getInterviewListByKeyword(searchValue);
      setSearchList(data);
    } else {
      setSearchList([]);
    }
  }, 300);

  const onChange = (e: InputChangeEvent) => {
    const searchValue = e.target.value;
    setValue(searchValue);
    handleSearch(searchValue);
  };

  return (
    <div className="fixed inset-0 z-40 flex items-start justify-center bg-black/50 pt-[200px]">
      <div className="flex h-[350px] w-[300px] flex-col gap-6 rounded-3xl bg-white p-6 shadow-md md:h-[400px] md:w-[600px] md:p-8">
        <div className="flex w-full flex-none gap-3 md:gap-6">
          <SearchBar value={value} onChange={onChange} />
          <button onClick={onClose}>
            <Icon id="close" size={24} className="h-auto md:w-[30px]" />
          </button>
        </div>
        <div className="flex min-h-0 w-full flex-1 flex-col gap-3">
          <div className="flex-none text-lg font-semibold">오늘의 질문</div>
          <div className="flex flex-1 flex-col gap-2 overflow-y-auto">
            {searchList && searchList.length > 0 ? (
              searchList.map((item, id) => (
                <MenuItem
                  key={id}
                  title={item.question}
                  onInterviewClick={() => onInterviewClick(item.interviewAnswerId)}
                />
              ))
            ) : (
              <div>검색 결과가 없습니다.</div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchModal;
