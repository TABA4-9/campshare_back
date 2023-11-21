import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch,faArrowRight } from '@fortawesome/free-solid-svg-icons';

import {useEffect} from "react";
import {useState} from "react";

import {Link} from "react-router-dom";

import { useRecoilState } from 'recoil';
import { campingItem } from '../data/campingItem';

import picture1 from '../assets/picture1.jpg';

export default function Home() {
    let [userSearch, setUserSearch] = useState<string>();
    let [campItem, setCampItem] = useRecoilState<CampingItemType[]>(campingItem);

    const onChange = (e: React.ChangeEvent<HTMLInputElement>):void => {
        setUserSearch(e.target.value)
    }

    useEffect(()=>{
        fetch("/api/item")
        .then(res=>res.json())
        .then(data=>{
            setCampItem(data)
        })
    }, [])
    
    console.log(campItem)

    return (
        <div className="flex flex-col px-10">
            <div className="flex flex-col justify-between rounded-3xl bg-black shrink-0 bg-contain text-white" style={{ minHeight:"80%", backgroundImage: `url(${picture1})`, backgroundSize: 'cover', backgroundPosition: 'center' }}>
                <div/>
                <div className="flex flex-col align-middle pt-20 pl-20">
                    <div className="text-5xl">
                        CampShare
                    </div>
                    <div className="flex flex-col min-h-[4/12] max-w-[6/12] text-2xl pt-8 pb-40">
                        <strong className="pb-5">캠핑, 부담은 감소하고 즐거움은 증가하다.</strong>
                        <div className="flex flex-col justify-between min-h-[42px] max-w-xs bg-white rounded-md">
                            <div/>
                            <div className="ml-3">
                                <input 
                                    className="w-64 text-black text-lg outline-none"
                                    type="text"
                                    value={userSearch}
                                    onChange={onChange}
                                />
                                {/* 아이콘을 눌렀을 때 해당 페이지로 이동하도록.. 수정... */}
                                <FontAwesomeIcon className="text-[24px] text-black pl-4" icon={faSearch} />
                            </div>
                            <div/>
                        </div>
                    </div>
                </div>
                <div/>
            </div>

            <div className="flex justify-center mt-10">
                <div className="flex flex-col">
                    <div className="font-bold text-3xl pb-5">대여 가능 용품</div>
                    <Link to="/categories"><button type="button" className="w-[120px] text-gray-900 bg-white border border-gray-300 focus:outline-none hover:bg-gray-100 focus:ring-4 focus:ring-gray-200 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-gray-800 dark:text-white dark:border-gray-600 dark:hover:bg-gray-700 dark:hover:border-gray-600 dark:focus:ring-gray-700">더보기 <FontAwesomeIcon icon={faArrowRight} /></button></Link>
                </div>
                <div>
                    <div className="flex px-10">
                        {
                            // 만약 index가 홀수라면 사진 padding or margin을 다르게 줘서 위아래로 만들기
                            campItem.slice(0,3).map((item,index)=>{
                                if(index >4) return null;
                                return (
                                    <div className={`w-[299px] h-[436px] bg-white border border-white flex-col justify-start items-start gap-3 inline-flex ${index % 2 === 0 && 'pt-14'}`}>
                                        <Link to={`/detail/${item.id}`} state={{item : item}}><img className="w-[200px] h-[200px]" src={item.image} alt="camping item img"/></Link>
                                        <div className="flex-col justify-start items-start flex">
                                            <div className="text-stone-900 text-lg font-medium font-['Poppins']">{item.name}</div>
                                            <div className="text-stone-900 text-opacity-50 text-lg font-medium font-['Poppins']">{item.price} ₩ / 일</div>
                                        </div>
                                    </div>
                                )
                            })
                        }
                    </div>
                </div>
            </div>

            


        </div>
    )
}