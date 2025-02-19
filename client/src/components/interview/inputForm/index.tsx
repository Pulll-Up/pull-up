import Icon from '@/components/common/icon';
import { TextAreaChangeEvent, TextAreaKeyboardEvent } from '@/types/event';

interface InputFormProps {
  id: string;
  placeholder: string;
  value: string;
  limit?: number;
  onChange: (e: TextAreaChangeEvent) => void;
  onSubmit: () => void;
  onKeyDown: (e: TextAreaKeyboardEvent) => void;
}

const InputForm = ({ id, placeholder, value, limit, onChange, onSubmit, onKeyDown }: InputFormProps) => {
  return (
    <div className="flex h-[100px] w-full gap-4 rounded-xl border border-stone-400 bg-white pb-2 pl-4 pr-2 pt-4 md:h-[144px]">
      <textarea
        id={id}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        onKeyDown={onKeyDown}
        className="h-full flex-1 resize-none overflow-y-auto border-none placeholder:text-stone-400 focus:outline-none lg:text-lg [&::-webkit-scrollbar]:hidden"
      ></textarea>
      <div className="flex h-full items-end gap-1">
        {limit ? (
          <div className="pb-1 font-semibold lg:text-lg">
            <span className="text-primary-500">{`${value.length}`}</span>
            <span className="text-stone-900">{`/${limit}`}</span>
          </div>
        ) : (
          <></>
        )}
        <button onClick={onSubmit}>
          {limit ? (
            <Icon id="upload-black" size={36} className="h-auto md:w-12" />
          ) : (
            <Icon id="upload-gray" size={36} className="h-auto md:w-12" />
          )}
        </button>
      </div>
    </div>
  );
};

export default InputForm;
