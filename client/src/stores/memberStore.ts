import { Member } from '@/types/member';
import { create } from 'zustand';
import { persist, createJSONStorage } from 'zustand/middleware';

interface MemberState {
  member: Member | null;
  isLoggedIn: boolean;
  isSolvedToday: boolean;
  interviewAnswerId: string;
  setMember: (member: Member) => void;
  setIsSolvedToday: (isSolvedToday: boolean) => void;
  setIsLoggedIn: (isLoggedIn: boolean) => void;
  setInterviewAnswerId: (interviewAnswerId: string) => void;
  logoutMember: () => void;
}

const initialState = {
  member: null,
  isLoggedIn: false,
  isSolvedToday: false,
  interviewAnswerId: '0',
};

export const memberStore = create<MemberState>()(
  persist(
    (set) => ({
      ...initialState,
      setMember: (member) => set({ member }),
      setIsSolvedToday: (isSolvedToday) => set({ isSolvedToday }),
      setIsLoggedIn: (isLoggedIn) => set({ isLoggedIn }),
      setInterviewAnswerId: (interviewAnswerId) => set({ interviewAnswerId }),
      logoutMember: () => {
        set(initialState);
        sessionStorage.removeItem('member-storage');
      },
    }),
    {
      name: 'member-storage',
      storage: createJSONStorage(() => sessionStorage),
      partialize: (state) => ({
        member: state.member?.email ? { email: state.member.email } : null,
        isLoggedIn: state.isLoggedIn,
        isSolvedToday: state.isSolvedToday,
        interviewAnswerId: state.interviewAnswerId,
      }),
    },
  ),
);
