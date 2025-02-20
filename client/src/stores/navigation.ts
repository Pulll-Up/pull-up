import { create } from 'zustand';

interface NavigationState {
  allowedNavigation: boolean;
  setAllowedNavigation: (value: boolean) => void;
}

export const useNavigationStore = create<NavigationState>((set) => ({
  allowedNavigation: false,
  setAllowedNavigation: (value) => set({ allowedNavigation: value }),
}));
