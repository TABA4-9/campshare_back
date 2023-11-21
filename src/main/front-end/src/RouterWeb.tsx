import {Routes, Route} from "react-router-dom"
import {useState} from "react";

import Home from "./pages/Home"
import Posts from "./pages/Posts";
import Detail from "./pages/Detail";
import Login from "./pages/Login";
import Kakaoauth from "./components/Kakaoauth";

export default function RouterWeb() {
    // 로그인 기능 구현 시, false로 바꿔서 로그인 유무에 따라 true, false로 바꿔줘야 함.
    const [isLogged, setIsLogged] = useState<Boolean>(false);
    return (
        <div>

            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/loginOauth2" element={<Login/>}/>
                <Route path="/detail/:id" element={<Detail/>}></Route>
                <Route path="/login/oauth2/code/kakao" element={<Kakaoauth/>}/>
                {/* <Route path="/category" element={<Category/>}></Route>
                <Route path="/recommand" element={<Recommand/>}></Route>
                {
                    isLogged 
                    ? <Route path="/myprofile" element={<Myprofile/>}></Route> 
                    : <Route path="/login" element={<UserLogin/>}
                } */}
                <Route path="/posts" element={<Posts/>}/>
            </Routes>

        </div>
    )
}