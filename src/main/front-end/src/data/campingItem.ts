import {atom } from 'recoil';

export const campingItem = atom<CampingItemType[]>({
    key : 'AllCampingItem',
    default : [],
})