import { Member, Subject } from '@/types/member';
import api from './instance';
import { useMutation, useQuery } from '@tanstack/react-query';
import { queryClient } from '@/main';
import { toast } from 'react-toastify';

export const getMember = async () => {
  const response = await api.get<Member>('member/me').json();
  return response;
};

export const useGetMember = () => {
  return useQuery({
    queryKey: ['member'],
    queryFn: getMember,
  });
};

// 선호 과목 수정
export const updateInterestSubjects = async (subjects: Subject[]) => {
  const response = await api.patch('member/interest-subject', {
    json: { subjectNames: subjects },
  });
  if (!response.ok) {
    toast.error('관심 과목이 수정되지 않았습니다. 다시 시도해주세요!', {
      position: 'bottom-center',
      toastId: 'subject-update',
    });
    throw new Error('관심 과목 수정 실패');
  }
  // PATCH 요청 성공 후 최신 멤버 데이터 가져오기
  return getMember();
};

export const useUpdateInterestSubjects = () => {
  return useMutation({
    mutationFn: (subjects: Subject[]) => updateInterestSubjects(subjects),

    onMutate: async (subjects) => {
      await queryClient.cancelQueries({ queryKey: ['member'] });
      const previousData = queryClient.getQueryData<Member | null>(['member']);

      if (previousData) {
        queryClient.setQueryData<Member>(['member'], {
          ...previousData,
          interestSubjects: subjects, // Optimistic UI 반영
        });
      }
      return { previousData };
    },
    onError: (err, _, context) => {
      if (context?.previousData) {
        queryClient.setQueryData(['member'], context.previousData);
      }
    },

    onSuccess: (updatedMember) => {
      // 최신 데이터를 UI에 반영
      queryClient.setQueryData(['member'], updatedMember);
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['member'] }); // 최신 데이터 강제 요청
    },
  });
};

// 디바이스 토큰 등록
export const registerDeviceToken = async (token: string) => {
  const data = await api.post('member/device-token', { json: { deviceToken: token } }).json<number>();
  return data;
};
