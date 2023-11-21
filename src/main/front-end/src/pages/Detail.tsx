import { useLocation } from "react-router-dom"

import { useState, useEffect } from "react";

export default function Detail() {
    const [itemInfo, setItemInfo] = useState<CampingItemType>()
    const location = useLocation();
    const data = location.state.item;
    

    useEffect(()=>{
        if(data) {
            console.log("data있음.");
            setItemInfo(data);
        }
    },[])

    return (
        <div className="flex px-10">
            <div className="flex rounded-md h-ful w-full bg-slate-300">
                <div className="flex my-5">
                    <div>
                        사진
                    </div>
                    <div>
                        내용
                    </div>
                </div>
            </div>
        </div>
    )
}